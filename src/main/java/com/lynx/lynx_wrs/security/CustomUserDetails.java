package com.lynx.lynx_wrs.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;

/**
 * CustomUserDetails
 * เป็นคลาสที่ใช้สำหรับเก็บข้อมูลผู้ใช้ที่เข้าสู่ระบบ ใช้สำหรับการจัดการสิทธิ์และข้อมูลผู้ใช้ในระบบ
 * สร้างขึ้นเพื่อให้สามารถเพิ่มข้อมูลเพิ่มเติมได้ตามต้องการ เช่น userId, email, name เป็นต้น
 * Created: 6/8/2025
 */
public class CustomUserDetails implements UserDetails, Serializable {
    private final Long userId; // อยากพกอะไรก็เพิ่ม เช่น email/name
    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;

    // flags พื้นฐาน
    private final boolean accountNonExpired;
    private final boolean accountNonLocked;
    private final boolean credentialsNonExpired;
    private final boolean enabled;

    public CustomUserDetails(
            Long userId,
            String username,
            String password,
            Collection<? extends GrantedAuthority> authorities
    ) {
        this(userId, username, password, authorities, true, true, true, true);
    }

    /**
     * CustomUserDetails
     * คอนสตรัคเตอร์สำหรับสร้าง CustomUserDetails ด้วยข้อมูลที่ระบุ
     * @param userId                รหัสผู้ใช้
     * @param username              ชื่อผู้ใช้
     * @param password              รหัสผ่าน
     * @param authorities           สิทธิ์ของผู้ใช้
     * @param accountNonExpired     สถานะบัญชีไม่หมดอายุ
     * @param accountNonLocked      สถานะบัญชีไม่ถูกล็อก
     * @param credentialsNonExpired สถานะข้อมูลประจำตัวไม่หมดอายุ
     * @param enabled               สถานะการใช้งานบัญชี
     */
    public CustomUserDetails(
            Long userId,
            String username,
            String password,
            Collection<? extends GrantedAuthority> authorities,
            boolean accountNonExpired,
            boolean accountNonLocked,
            boolean credentialsNonExpired,
            boolean enabled
    ) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
    }

    public Long getUserId() { return userId; }

    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }// authorities ที่ผู้ใช้มี
    @Override public String getPassword() { return password; }
    @Override public String getUsername() { return username; }
    @Override public boolean isAccountNonExpired() { return accountNonExpired; }// ถ้าบัญชีหมดอายุจะไม่สามารถเข้าสู่ระบบได้
    @Override public boolean isAccountNonLocked() { return accountNonLocked; }// ถ้าบัญชีถูกล็อกจะไม่สามารถเข้าสู่ระบบได้
    @Override public boolean isCredentialsNonExpired() { return credentialsNonExpired; }// ถ้าข้อมูลประจำตัวหมดอายุจะไม่สามารถเข้าสู่ระบบได้
    @Override public boolean isEnabled() { return enabled; }// ถ้าบัญชีถูกปิดใช้งานจะไม่สามารถเข้าสู่ระบบได้
}
