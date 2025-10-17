package com.lynx.lynx_wrs.security;

import com.lynx.lynx_wrs.db.entities.Users;
import com.lynx.lynx_wrs.db.repositories.UserRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * CustomUserDetailsService
 * เป็นคลาสที่ใช้สำหรับโหลดข้อมูลผู้ใช้จากฐานข้อมูลและสร้าง CustomUserDetails
 * ใช้สำหรับการตรวจสอบข้อมูลผู้ใช้ในกระบวนการเข้าสู่ระบบ
 * Created: 9/8/2025
 * @Updated: 18/8/2025
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * loadUserByUsername
     * ใช้สำหรับโหลดข้อมูลผู้ใช้จากฐานข้อมูลตามอีเมล
     * @param email อีเมลของผู้ใช้ที่ต้องการค้นหา
     * @return UserDetails ที่ประกอบด้วยข้อมูลผู้ใช้และสิทธิ์การเข้าถึง
     * @throws UsernameNotFoundException หากไม่พบผู้ใช้ตามอีเมลที่ระบุ
     */
    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "userAuth", key = "#email.toLowerCase()")// ใช้ Cacheable เพื่อเก็บผลลัพธ์การค้นหา User ตาม username
    public UserDetails loadUserByUsername(String email) {
        Users user = userRepository.findByEmail(email);
        List<SimpleGrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
        // สร้าง CustomUserDetails ด้วยค่าพื้นฐาน (primitive) + authorities ที่ "พร้อมใช้"
        return new CustomUserDetails(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }

    /**
     * loadUserByUsername
     * ใช้สำหรับโหลดข้อมูลผู้ใช้จากฐานข้อมูลตามอีเมล
     * @param id id ของผู้ใช้
     * @return UserDetails ที่ประกอบด้วยข้อมูลผู้ใช้และสิทธิ์การเข้าถึง
     * @throws UsernameNotFoundException หากไม่พบผู้ใช้ตามอีเมลที่ระบุ
     */
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "userAuth", key = "'ID:' + #id")// ใช้ Cacheable เพื่อเก็บผลลัพธ์การค้นหา User ตาม username
    public UserDetails loadUserById(Long id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found id=" + id));
        // ดึง roles ของ user และแปลงเป็น SimpleGrantedAuthority
        List<SimpleGrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

        // สร้าง CustomUserDetails ด้วยค่าพื้นฐาน (primitive) + authorities ที่ "พร้อมใช้"
        return new CustomUserDetails(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }
}
