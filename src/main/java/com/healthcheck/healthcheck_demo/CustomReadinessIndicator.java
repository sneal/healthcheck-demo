package com.healthcheck.healthcheck_demo;

import org.springframework.boot.actuate.availability.ReadinessStateHealthIndicator;
import org.springframework.boot.availability.ApplicationAvailability;
import org.springframework.boot.availability.AvailabilityState;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * CustomReadinessIndicator is a custom implementation of the ReadinessStateHealthIndicator.
 * It checks the readiness state of the application to determine if it is ready to accept traffic.
 */
@Component
public class CustomReadinessIndicator extends ReadinessStateHealthIndicator {

    private final H2Repository h2Repository;

    private final CacheManager cacheManager;

    private final RestTemplate restTemplate;

    /**
     * Constructor that initializes the CustomReadinessIndicator with the ApplicationAvailability instance.
     *
     * @param availability the application availability instance
     */

    public CustomReadinessIndicator(ApplicationAvailability availability, H2Repository h2Repository, CacheManager cacheManager, RestTemplate restTemplate) {
        super(availability);
        this.h2Repository = h2Repository;
        this.cacheManager = cacheManager;
        this.restTemplate = restTemplate;
    }

    /**
     * Overrides the getState method to provide custom logic for determining the readiness state.
     *
     * @param applicationAvailability the application availability instance
     * @return the readiness state of the application
     */
    @Override
    protected AvailabilityState getState(ApplicationAvailability applicationAvailability) {
        System.out.println("Readiness check initiated...");
        if (checkReadiness()) {
            System.out.println("Readiness check passed!");
            return ReadinessState.ACCEPTING_TRAFFIC;
        } else {
            System.out.println("Readiness check failed.");
            return ReadinessState.REFUSING_TRAFFIC;
        }
    }

    /**
     * Custom method to check the readiness of the application.
     *
     * @return true if the application is ready to accept traffic, false otherwise
     */
    private boolean checkReadiness() {
        return checkH2DB() && checkCache() && checkExternalAPI();
    }

    private boolean checkCache() {
        System.out.println("Checking Cache...");
        Cache cache = cacheManager.getCache("testCache");
        if (cache == null) {
            System.out.println("Cache check failed, cache not initialized.");
            return false;
        }
        if (cache.get("populated") == null) {
            System.out.println("Cache check failed, cache not warmed up.");
            return false;
        }
        System.out.println("Cache check passed.");
        return true;
    }

    private boolean checkH2DB() {
        System.out.println("Checking H2 Database...");
        try {
            h2Repository.findAll();
        } catch (Exception e) {
            System.out.println("H2 Database check failed: " + e.getMessage());
            return false;
        }
        System.out.println("H2 Database check passed.");
        return true;
    }

    private boolean checkExternalAPI() {
        System.out.println("Checking external API...");
        try {
            restTemplate.getForEntity("https://api.github.com", String.class);
        }
        catch (RestClientException e) {
            System.out.println("External API check to api.github.com failed: " + e.getMessage());
            return false;
        }
        System.out.println("External API check to api.github.com passed.");
        return true;
    }
}