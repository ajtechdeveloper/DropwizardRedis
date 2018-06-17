package com.aj.dropwizardredis.resource;

import com.aj.dropwizardredis.DropwizardRedisConfiguration;
import com.codahale.metrics.health.HealthCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DropwizardRedisHealthCheckResource extends HealthCheck {

    private static final Logger logger = LoggerFactory.getLogger(DropwizardRedisHealthCheckResource.class);

    private static String appName;

    public DropwizardRedisHealthCheckResource(DropwizardRedisConfiguration dropwizardRedisConfiguration){
       this.appName = dropwizardRedisConfiguration.getAppName();
    }

    @Override
    protected Result check() throws Exception {
        logger.info("App Name is: {}", appName);
        if("DropwizardRedis".equalsIgnoreCase(appName)) {
            return Result.healthy();
        }
        return Result.unhealthy("Basic Dropwizard Service is down");
    }
}