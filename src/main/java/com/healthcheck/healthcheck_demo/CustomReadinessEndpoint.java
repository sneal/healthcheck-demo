package com.healthcheck.healthcheck_demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

@Component
@Endpoint(id = "customReadiness")
public class CustomReadinessEndpoint {

    private final HealthIndicator customReadinessIndicator;

    public CustomReadinessEndpoint(HealthIndicator customReadinessIndicator) {
        this.customReadinessIndicator = customReadinessIndicator;
    }

    @ReadOperation
    public Health getReadiness() {
        return customReadinessIndicator.health();
    }
}
