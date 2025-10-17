package com.lynx.lynx_wrs.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JwtAuthenticationFilter
 * คลาสนี้ใช้สำหรับกรองคำขอ HTTP และตรวจสอบ JWT token ที่แนบมากับคำขอ
 * หาก JWT token ถูกต้อง จะทำการตั้งค่า Authentication ใน SecurityContextHolder
 * เพื่อให้สามารถเข้าถึงข้อมูลผู้ใช้ได้ในส่วนอื่นๆ ของแอปพลิเคชัน
 * Created: 15/8/2025
 * @Updated: 18/8/2025
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired private JwtUtils jwtUtils;
    @Autowired private CustomUserDetailsService customUserDetailsService;
    @Autowired private AuthRevokeService authRevokeService;

    /**
     * doFilterInternal
     * เมธอดนี้จะถูกเรียกในแต่ละคำขอ HTTP เพื่อกรองและตรวจสอบ JWT token
     * โดยจะดึง token จาก header ของคำขอ และตรวจสอบความถูกต้องหาก token ถูกต้อง จะโหลดข้อมูลผู้ใช้จาก CustomUserDetailsService
     * ตั้งค่า Authentication ใน SecurityContextHolder เพื่อให้สามารถเข้าถึงข้อมูลผู้ใช้ได้ในส่วนอื่นๆ ของแอปพลิเคชัน
     * @param request คำขอ HTTP ที่เข้ามา
     * @param response คำตอบ HTTP ที่จะส่งกลับ
     * @param chain โซ่ของฟิลเตอร์ที่ต้องดำเนินการต่อไป
     * @throws ServletException ถ้ามีข้อผิดพลาดเกิดขึ้นระหว่างการประมวลผลคำขอ
     * @throws IOException ถ้ามีข้อผิดพลาดเกิดขึ้นระหว่างการอ่านหรือเขียนข้อมูล
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain)
            throws ServletException, IOException {
        String token = getJwtFromRequest(request);

        // ตรวจสอบว่า token ไม่เป็น null และ AuthenticationContext ยังไม่มีการตั้งค่า
        if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            Long userId = jwtUtils.extractUserId(token);
            String email = jwtUtils.extractUsernameSafe(token);

            if (email != null && jwtUtils.validateToken(token, email)) {

                // ตรวจ revokedAt (ถ้ามี)
                Long iatMillis = jwtUtils.extractIssuedAtMillis(token);
                Long revokedAt = authRevokeService.getRevokedAt(userId);
                // ถ้า token ถูกเพิกถอนแล้ว (revokedAt มีค่า และ iat < revokedAt) ให้ข้ามการตั้งค่า Authentication
                if (revokedAt != null && iatMillis != null && iatMillis < revokedAt) {
                    chain.doFilter(request, response);
                    return;
                }
                // โหลดข้อมูลผู้ใช้จาก CustomUserDetailsService โดยใช้ username ที่ดึงมาจาก token
                UserDetails userDetails = customUserDetailsService.loadUserById(userId);
                // ทำการตั้งค่า Authentication ใน SecurityContextHolder เพื่อให้สามารถเข้าถึงข้อมูลผู้ใช้ได้ในส่วนอื่นๆ ของแอปพลิเคชัน
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        chain.doFilter(request, response);
    }

    /**
     * getJwtFromRequest
     * เมธอดนี้ใช้สำหรับดึง JWT token จาก header ของคำขอ HTTP
     * @param request คำขอ HTTP ที่เข้ามา
     * @return JWT token หากมีอยู่ใน header; ถ้าไม่มีคืนค่า null
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        return (bearer != null && bearer.startsWith("Bearer ")) ? bearer.substring(7) : null;
    }
}