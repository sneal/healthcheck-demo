package com.healthcheck.healthcheck_demo;

import org.springframework.boot.actuate.availability.LivenessStateHealthIndicator;
import org.springframework.boot.availability.ApplicationAvailability;
import org.springframework.boot.availability.AvailabilityState;
import org.springframework.boot.availability.LivenessState;
import org.springframework.stereotype.Component;

/**
 * CustomLivenessIndicator is a custom implementation of the LivenessStateHealthIndicator.
 * It checks the liveness state of the application to determine if it is healthy and able
 * to continue serving traffic.
 */
@Component
public class CustomLivenessIndicator extends LivenessStateHealthIndicator {

    /**
     * Constructor that initializes the CustomLivenessIndicator with the ApplicationAvailability instance.
     *
     * @param availability the application availability instance
     */
    public CustomLivenessIndicator(ApplicationAvailability availability) {
        super(availability);
    }

    /**
     * Overrides the getState method to provide custom logic for determining the liveness state.
     *
     * @param applicationAvailability the application availability instance
     * @return the liveness state of the application
     */
    @Override
    protected AvailabilityState getState(ApplicationAvailability applicationAvailability) {
        System.out.println("Liveness check initiated...");
        if (checkLiveness()) {
            System.out.println("Liveness check passed!");
            return LivenessState.CORRECT;
        } else {
            System.out.println("Liveness check failed.");
            return LivenessState.BROKEN;
        }
    }

    /**
     * Custom method to check the liveness of the application.
     *
     * @return true if the application is statically configured and deployed properly, false otherwise
     */
    private boolean checkLiveness() {
        // TODO validate config settings, certs, and other basic runtime requirements on startup elsewhere
        return true;
    }
}
