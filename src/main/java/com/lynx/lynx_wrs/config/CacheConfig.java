package com.lynx.lynx_wrs.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.TimeUnit;

/**
 * CacheConfig
 * class นี้ใช้สำหรับกำหนดค่า Cache ในแอปพลิเคชัน Spring Boot
 * โดยใช้ Caffeine เป็น Cache Provider มันจะสร้าง CacheManager ที่มีการกำหนดค่า Cache หลักและ Cache ที่กำหนดเอง
 * Created: 9/8/2025
 * Updated: 2/9/2025
 */
@Configuration
@EnableCaching
@EnableScheduling
public class CacheConfig {

    /**
     * cacheManager
     * สร้างและกำหนดค่า CacheManager ที่ใช้ Caffeine เป็น Cache Provider
     * โดยมีการกำหนด Cache หลักและ Cache ที่กำหนดเอง พร้อมกับนโยบายการหมดอายุและขนาดสูงสุด
     *
     * @return CacheManager ที่กำหนดค่าแล้ว
     */
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager m = new CaffeineCacheManager("userAuth", "oneTime", "authRevoke", "refresh");
        m.setCaffeine(Caffeine.newBuilder().expireAfterWrite(3, TimeUnit.MINUTES).maximumSize(10000));
        m.registerCustomCache("oneTime",
                Caffeine.newBuilder().expireAfterWrite(15, TimeUnit.MINUTES).maximumSize(10000).build());
        m.registerCustomCache("refresh",
                Caffeine.newBuilder().expireAfterWrite(1, TimeUnit.DAYS).maximumSize(10000).build()); //
        m.setCacheLoader((key) -> null);
        return m;
    }

}