package com.lynx.lynx_wrs.security;

import com.lynx.lynx_wrs.http.domain.users.services.RefreshSessionService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * JwtUtils
 * คลาสนี้ใช้สำหรับจัดการกับ JSON Web Tokens (JWT) ในแอปพลิเคชัน Spring Boot
 * มันมีฟังก์ชันสำหรับสร้าง, ตรวจสอบ, และดึงข้อมูลจาก JWT
 * Created: 6/8/2025
 * Updated: 2/9/2025
 */
@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secretKeyString;     // อาจเป็น raw หรือ Base64 ก็ได้

    @Value("${jwt.expiration}")
    private long AccessExpirationTime;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    @Autowired
    private RefreshSessionService refreshSessionService;
    private volatile SecretKey secretKey;

    /**
     * ensureKey
     * สร้างหรือดึง SecretKey สำหรับการเข้ารหัส JWT
     * ใช้ double-checked locking เพื่อให้ thread-safe
     *
     * @return SecretKey สำหรับการเข้ารหัส JWT
     */
    private SecretKey ensureKey() {
        if (secretKey == null) {
            synchronized (this) {
                if (secretKey == null) {
                    // ถ้าเก็บเป็น Base64 ให้ถอดก่อน; ถ้าเป็น raw string ให้ใช้ UTF-8 bytes
                    byte[] keyBytes = tryDecodeBase64(secretKeyString);
                    if (keyBytes == null) {
                        keyBytes = secretKeyString.getBytes(StandardCharsets.UTF_8);
                    }
                    secretKey = Keys.hmacShaKeyFor(keyBytes);
                }
            }
        }
        return secretKey;
    }

    /**
     * tryDecodeBase64
     * พยายามถอดรหัส Base64 จาก string ที่ให้มา
     * ถ้าไม่สำเร็จจะคืนค่า null
     *
     * @param value string ที่ต้องการถอดรหัส
     * @return byte[] ถอดรหัสแล้ว หรือ null ถ้าไม่สำเร็จ
     */
    private byte[] tryDecodeBase64(String value) {
        try {
            return Base64.getDecoder().decode(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * generateToken
     * สร้าง JWT token สำหรับผู้ใช้ โดยระบุ claims ที่ต้องการ
     *
     * @param email อีเมลของผู้ใช้ที่จะใช้เป็น subject ของ token
     * @return JWT token ที่สร้างขึ้น
     */
    public String generateToken(String email, Long userId) {
        SecretKey key = ensureKey();
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .claim("id", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + AccessExpirationTime))
                .setId(UUID.randomUUID().toString())
                .signWith(key)
                .compact();
    }

    /**
     * generateRefreshToken
     * สร้าง JWT refresh token สำหรับผู้ใช้
     *
     * @param email อีเมลของผู้ใช้ที่จะใช้เป็น subject ของ token
     * @return JWT refresh token ที่สร้างขึ้น
     */
    public String generateRefreshToken(String email , Long userId) {
        SecretKey key = ensureKey();
        return Jwts.builder()
                .setSubject(email)
                .claim("id", userId)
                .claim("typ", "refresh")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .setId(UUID.randomUUID().toString())
                .signWith(key)
                .compact();
    }


    /**
     * extractSubject
     * ดึง subject (อีเมล) จาก JWT token
     *
     * @param token JWT token ที่ต้องการดึงข้อมูล
     * @return อีเมลที่ถูกเก็บใน subject ของ token
     */
    public String extractSubject(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(ensureKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public Long extractUserId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(ensureKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        Object idObj = claims.get("id");
        if (idObj == null) return null;
        if (idObj instanceof Integer i) return i.longValue();
        if (idObj instanceof Long l) return l;
        if (idObj instanceof String s) return Long.valueOf(s);
        return null;
    }

    /**
     * extractUsernameSafe
     * ดึง subject (อีเมล) จาก JWT token โดยไม่ throw exception ถ้าไม่สำเร็จ
     *
     * @param token JWT token ที่ต้องการดึงข้อมูล
     * @return อีเมลที่ถูกเก็บใน subject ของ token หรือ null ถ้าไม่สำเร็จ
     */
    public String extractUsernameSafe(String token) {
        try {
            return extractSubject(token);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * isTokenExpired
     * ตรวจสอบว่า JWT token หมดอายุหรือไม่
     *
     * @param token JWT token ที่ต้องการตรวจสอบ
     * @return true ถ้า token หมดอายุ, false ถ้ายังไม่หมดอายุ
     */
    public boolean isTokenExpired(String token) {
        Date exp = Jwts.parserBuilder()
                .setSigningKey(ensureKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return exp.before(new Date());
    }

    /**
     * extractIssuedAtMillis
     * ดึงเวลาที่ token ถูกสร้าง (issued at) ในรูปแบบ milliseconds ใช้สำหรับตรวจสอบ token ที่ถูกเพิกถอน
     *
     * @param token JWT token ที่ต้องการดึงข้อมูล
     * @return เวลาที่ token ถูกสร้างใน milliseconds หรือ null ถ้าไม่มีข้อมูล
     */
    public Long extractIssuedAtMillis(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(ensureKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        Date iat = claims.getIssuedAt();
        return (iat != null) ? iat.getTime() : null;
    }


    /**
     * validateToken
     * ตรวจสอบว่า JWT token ถูกต้องและยังไม่หมดอายุ
     *
     * @param token    JWT token ที่ต้องการตรวจสอบ
     * @param username อีเมลที่คาดว่าจะเป็น subject ของ token
     * @return true ถ้า token ถูกต้องและยังไม่หมดอายุ, false ถ้าไม่ถูกต้องหรือหมดอายุ
     */
    public boolean validateToken(String token, String username) {
        try {
            return username.equals(extractSubject(token)) && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * parseClaims
     * ดึง Claims ทั้งหมดจาก JWT token
     * @param token JWT token ที่ต้องการดึงข้อมูล
     * @return Claims ที่ถูกเก็บใน token
     */
    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(ensureKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }



    /**
     * extractJti
     * ดึงค่า "jti" (JWT ID) claim จาก JWT token เพื่อระบุ ID ของ token ว่าเป็น token ใด
     * @param token JWT token ที่ต้องการดึงข้อมูล
     * @return ค่า "jti" หรือ null ถ้าไม่มีหรือเกิดข้อผิดพลาด
     */
    public String extractJti(String token) {
        try {
            return parseClaims(token).getId(); // jti
        } catch (Exception e) {
            return null;
        }
    }



    /**
     * validateRefreshToken
     * ตรวจสอบว่า JWT token เป็น refresh token ที่ถูกต้องและยังไม่หมดอายุ
     * @param token JWT token ที่ต้องการตรวจสอบ
     * @return true ถ้า token เป็น refresh token ที่ถูกต้องและยังไม่หมดอายุ, false ถ้าไม่ถูกต้องหรือหมดอายุ
     */
    public boolean validateRefreshToken(String token) {
        try {
            Claims c = parseClaims(token);
            Object typ = c.get("typ");
            String jti = c.getId();

            //  เช็คว่า typ เป็น refresh
            if (typ == null || !"refresh".equals(typ.toString())) return false;

            //  เช็คว่า jti ไม่ถูกใช้ไปแล้ว
            if (jti == null || refreshSessionService.isRevoked(jti)) return false;

            //  เช็ควันหมดอายุ (ป้องกัน token หมดอายุ)
            Date exp = c.getExpiration();
            if (exp.before(new Date())) return false;

            return true;
        } catch (Exception e) {
            return false;
        }
    }



}
