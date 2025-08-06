package com.bookingplatform.config;


import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {
    public static final String BOOKING_SEATS_CACHE = "bookingSeatsCache";
    public static final String MOVIES_CACHE = "moviesCache";
    public static final String USERS_CACHE = "usersCache";

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();

        // Configure the movies cache with a TTL of 1 hour
        cacheManager.registerCustomCache(MOVIES_CACHE,
                Caffeine.newBuilder()
                        .expireAfterWrite(1, TimeUnit.HOURS)
                        .maximumSize(10000)
                        .recordStats()
                        .buildAsync());

        // Configure the users cache with a TTL of 30 minutes
        cacheManager.registerCustomCache(USERS_CACHE,
                Caffeine.newBuilder()
                        .expireAfterWrite(30, TimeUnit.MINUTES)
                        .maximumSize(500)
                        .recordStats()
                        .buildAsync());

        // Configure the booking seats cache with a TTL of 5 minutes
        cacheManager.registerCustomCache(BOOKING_SEATS_CACHE,
                Caffeine.newBuilder()
                        .expireAfterWrite(5, TimeUnit.MINUTES)
                        .maximumSize(1000)
                        .recordStats()
                        .buildAsync());

        return cacheManager;
    }
}