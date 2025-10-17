package com.lynx.lynx_wrs.security;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

/**
 * AuthRevokeService
 * class นี้ใช้สำหรับจัดการการเพิกถอนสิทธิ์การเข้าถึง (revoke) ของผู้ใช้ในระบบ
 * โดยใช้ Cache เพื่อเก็บเวลาที่มีการเพิกถอนสิทธิ์ล่าสุดสำหรับแต่ละผู้ใช้
 * เมื่อมีการเปลี่ยนแปลงสิทธิ์ของผู้ใช้ เช่น การเปลี่ยนแปลงบทบาท (role)
 * จะเรียกใช้เมธอด revoke() เพื่อบันทึกเวลาปัจจุบันลงใน Cache
 * และเมธอด getRevokedAt() ใช้เพื่อดึงเวลาที่มีการเพิกถอนสิทธิ์ล่าสุดของผู้ใช้
 * Created: 15/8/2025
 */
@Service
@RequiredArgsConstructor
public class AuthRevokeService {
    private final CacheManager cacheManager;

    /** ดึง Cache ที่ใช้เก็บข้อมูลการเพิกถอนสิทธิ์ */
    private Cache cache() {
        return cacheManager.getCache("authRevoke");
    }
    /** สร้าง key สำหรับเก็บข้อมูลใน Cache โดยใช้ email ของผู้ใช้ */
    private String key(Long userId) {
        return "REVOKED_AT:" + userId;
    }

    /** เรียกตอนเปลี่ยนสิทธิ์ role เพื่อให้ token เก่าใช้ไม่ได้ทันที */
    public void revoke(Long userId) {
        // เก็บเป็น millis epoch
        cache().put(key(userId), System.currentTimeMillis());
    }

    /** อ่านเวลาที่เพิกถอนล่าสุด ถ้าไม่มี (null) แปลว่าไม่เคย revoke */
    public Long getRevokedAt(Long userId) {
        return cache().get(key(userId), Long.class);
    }
}
