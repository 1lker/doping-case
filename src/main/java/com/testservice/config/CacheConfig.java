package com.testservice.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Arrays.asList(
                new ConcurrentMapCache("students"),
                new ConcurrentMapCache("student"),
                new ConcurrentMapCache("studentByNumber"),
                new ConcurrentMapCache("tests"),
                new ConcurrentMapCache("test"),
                new ConcurrentMapCache("participations"),
                new ConcurrentMapCache("participation"),
                new ConcurrentMapCache("participationsByStudent"),
                new ConcurrentMapCache("participationsByTest")
        ));
        return cacheManager;
    }
}