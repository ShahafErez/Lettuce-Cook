package com.shahaf.api_gateway.configuration;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.shahaf.api_gateway.constants.ApplicationConstants.PATH_PREFIX;

@Configuration
public class ApiGatewayConfiguration {

    @Bean
    public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p.path(PATH_PREFIX + "/recipes/**").uri("lb://recipe-service"))
                .route(p -> p.path(PATH_PREFIX + "/favorite/**").uri("lb://recipe-service"))
                .route(p -> p.path(PATH_PREFIX + "/search/**").uri("lb://search-service"))
                .route(p -> p.path(PATH_PREFIX + "/auth/**").uri("lb://auth-service"))
                .build();
    }
}