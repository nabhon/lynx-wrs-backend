package com.lynx.lynx_wrs.config;

import com.lynx.lynx_wrs.security.CustomUserDetailsService;
import com.lynx.lynx_wrs.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * SecurityConfig
 * คลาสนี้ใช้สำหรับกำหนดค่าการตั้งค่าความปลอดภัยของแอปพลิเคชัน
 * ใช้ตั้งค่าการยืนยันตัวตน การเข้ารหัสรหัสผ่าน และกฎความปลอดภัยของ HTTP
 * Created: 15/8/2025
 */
@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService customUserDetailsService;

    /**
     * authenticationManager
     * กำหนดค่า AuthenticationManager โดยใช้ CustomUserDetailsService และ PasswordEncoder
     * @param cfg AuthenticationConfiguration ที่ใช้ในการตั้งค่า
     * @return AuthenticationManager ที่กำหนดค่าแล้ว
     * @throws Exception หากเกิดข้อผิดพลาดในการตั้งค่า
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }

    /**
     * passwordEncoder
     * กำหนดค่า การเข้ารหัสโดยใช้ BCryptPasswordEncoder
     * @return รหัสที่กำหนดค่าแล้ว
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12); // เพิ่ม strength
    }

    /**
     * securityFilterChain
     * กำหนดค่าการตั้งค่าความปลอดภัยของ HTTP
     * รวมถึงการปิด CSRF, การตั้งค่า CORS, การจัดการเซสชัน, กฎการอนุญาตคำขอ HTTP,
     * การจัดการข้อผิดพลาด และการเพิ่มฟิลเตอร์สำหรับ JWT Authentication
     * เป็นทางผ่านของคำขอ HTTP ทั้งหมด
     * @param http HttpSecurity ที่ใช้ในการตั้งค่า
     * @return SecurityFilterChain ที่กำหนดค่าแล้ว
     * @throws Exception หากเกิดข้อผิดพลาดในการตั้งค่า
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())// ปิดการใช้งาน CSRF เนื่องจากใช้ JWT
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))// ตั้งค่า CORS
                /** ตั้งค่านโยบายการจัดการเซสชันเป็น STATELESS เนื่องจากใช้ JWT */
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                /** กำหนดกฎการอนุญาตคำขอ HTTP */
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/login",
                                "/api/auth/register/**",
                                "/api/auth/password/**",
                                "/api/tasks/**",
                                "/api/tasks",
                                "/api/auth/refresh",
                                "/api/projects/**"
                        ).permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/moderetor/**").hasAnyRole("MODERATOR", "ADMIN")
                        .requestMatchers(
                                "/api/user/**",
                                "/api/auth/user",
                                "/api/users",
                                "/api/community/**")
                        .hasAnyRole("USER", "ADMIN","MODERATOR")
                        .anyRequest().authenticated()
                )
                /** จัดการข้อผิดพลาดที่เกิดขึ้นระหว่างการยืนยันตัวตนและการอนุญาต */
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((req, res, e) -> {
                            res.setStatus(HttpStatus.UNAUTHORIZED.value());
                            res.setContentType("application/json");
                            res.getWriter().write("{\"message\":\"Unauthorized\"}");
                        })
                        /** จัดการข้อผิดพลาดเมื่อผู้ใช้ไม่มีสิทธิ์เข้าถึงทรัพยากร */
                        .accessDeniedHandler((req, res, e) -> {
                            res.setStatus(HttpStatus.FORBIDDEN.value());
                            res.setContentType("application/json");
                            res.getWriter().write("{\"message\":\"Forbidden\"}");
                        })
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * corsConfigurationSource
     * กำหนดค่าการตั้งค่า CORS (Cross-Origin Resource Sharing)
     * โดยอนุญาตให้เข้าถึงจากต้นทางที่ระบุ และกำหนดวิธีการและหัวข้อที่อนุญาต
     * @return CorsConfigurationSource ที่กำหนดค่าแล้ว
     */
    @Value("${app.frontend.url}")private String frontendUrl;
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowCredentials(true);
        cfg.setAllowedOrigins(List.of(
                frontendUrl,
                "http://localhost:3000"
        ));
        cfg.setAllowedMethods(List.of("GET","POST","PUT","DELETE","PATCH","OPTIONS"));
        cfg.setAllowedHeaders(List.of("*"));
        cfg.setExposedHeaders(List.of("Authorization"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }
}
