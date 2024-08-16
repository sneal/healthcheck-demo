package com.healthcheck.healthcheck_demo;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * Custom component that simulates async warming a cache on start
 */
@Component
public class CacheWarmUp {
    private final CacheManager cacheManager;

    public CacheWarmUp(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Async
    @EventListener(ApplicationReadyEvent.class)
    public void warmUpCache() {
        System.out.println("Cache warm-up initiated...");

        Cache cache = cacheManager.getCache("testCache");
        if (cache == null) {
            throw new RuntimeException("testCache is null, check the Spring component wiring");
        }
        cache.put("testKey", "testValue");

        // Simulate delay to complete cache warm-up
        try {
            System.out.println("Simulating cache warm-up 2min delay...");
            Thread.sleep(2 * 60 * 1000);
            cache.put("populated", "true");
        } catch (InterruptedException e) {
            System.err.println("Cache warm-up interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        }

        System.out.println("Cache warm-up completed.");
    }
}
