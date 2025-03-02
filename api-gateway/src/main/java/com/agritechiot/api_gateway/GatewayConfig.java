package com.agritechiot.api_gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service-route", r -> r
                        .path("/user/**")
                        .filters(f -> f.addRequestHeader("X-Request-User", "Gateway"))
                        .uri("http://localhost:8081"))
                .route("order-service-route", r -> r
                        .path("/order/**")
                        .filters(f -> f.addRequestHeader("X-Request-Order", "Gateway"))
                        .uri("http://localhost:8082"))
                .build();
    }

}
