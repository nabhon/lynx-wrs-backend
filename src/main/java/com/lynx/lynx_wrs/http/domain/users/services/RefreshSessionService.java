package com.lynx.lynx_wrs.http.domain.users.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import com.lynx.lynx_wrs.http.exception.AppException;
import com.lynx.lynx_wrs.http.exception.ErrorCode;


/**
 * RefreshSessionService
 * บริการสำหรับจัดการ session ของ refresh token
 * ใช้ Cache เพื่อเก็บ jti ล่าสุดของ refresh token สำหรับแต่ละ email
 * เพื่อให้สามารถตรวจสอบได้ว่า token ที่ใช้งานเป็นใบล่าสุดหรือไม่
 * Created: 2/9/2025
 */
@Service
@RequiredArgsConstructor
public class RefreshSessionService {

    @Autowired
    private final CacheManager cacheManager;

    /** ใช้ cache ชื่อ "refresh" ที่กำหนดใน CacheConfig */
    private Cache cache() { return cacheManager.getCache("refresh"); }

    /** สร้าง key สำหรับเก็บใน cache โดยใช้ email เป็นส่วนหนึ่งของ key */
    private String key(String email) { return "LATEST_REFRESH_JTI:" + email.toLowerCase(); }

    /** บันทึก jti ล่าสุดของ refresh token สำหรับ email ที่ระบุ */
    public void setLatest(String email, String jti) { cache().put(key(email), jti); }

    /** ดึง jti ล่าสุดของ refresh token สำหรับ email ที่ระบุ */
    public String getLatest(String email) { return cache().get(key(email), String.class); }

    /** ตรวจสอบว่า jti ที่ระบุเป็น jti ล่าสุดสำหรับ email ที่ระบุหรือไม่ */
    public void assertLatest(String email, String jti) {
        String latest = getLatest(email);
        if (latest == null || jti == null || !jti.equals(latest)) {
            throw new AppException(ErrorCode.INVALID_JWT, "refresh token ไม่ถูกต้องหรือหมดอายุ");
        }
    }
    /** สร้าง key สำหรับเก็บ JTI ที่ถูก revoke */
    private String revokedKey(String jti) {
        return "REVOKED_JTI:" + jti;
    }

    /** บันทึก JTI ที่ถูก revoke (หลังจากใช้งานแล้ว) */
    public void revoke(String jti) {
        cache().put(revokedKey(jti), true);
    }

    /** ตรวจสอบว่า JTI นี้ถูก revoke แล้วหรือไม่ */
    public boolean isRevoked(String jti) {
        Boolean revoked = cache().get(revokedKey(jti), Boolean.class);
        return revoked != null && revoked;
    }


    /** ลบ jti ล่าสุดของ refresh token สำหรับ email ที่ระบุ (เช่น เมื่อผู้ใช้ logout) */
    public void clear(String email) { cache().evict(key(email)); }
}

