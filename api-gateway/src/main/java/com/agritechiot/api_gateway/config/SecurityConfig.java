package com.agritechiot.api_gateway.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

@RequiredArgsConstructor
@Configuration
@EnableWebFluxSecurity
@Slf4j
public class SecurityConfig {
    private final JwtAuthConverter jwtAuthConverter;
    private final String[] freeResourceUrls = {"/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**",
            "/swagger-resources/**", "/api-docs/**", "/aggregate/**", "/actuator/prometheus"};

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> {
                    log.info("Setting up route authorizations");
                    exchanges
                            .pathMatchers(freeResourceUrls).permitAll()
                            .pathMatchers(HttpMethod.OPTIONS).permitAll()
                            .anyExchange().permitAll();
                })

                .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        http.oauth2ResourceServer(oauth2 -> {
            log.info("Configuring OAuth2 Resource Server with JWT");
            oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter));
        });

        log.info("Security Web Filter Chain configuration completed");
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        log.info("Configuring CORS Source");
        CorsConfiguration configuration = new CorsConfiguration();

        // Use allowedOriginPatterns for flexibility (note: WebSocket handshake uses HTTP protocol)
        configuration.setAllowedOriginPatterns(List.of(
                "http://localhost:5173",
                "http://localhost:8083"  // Changed from ws:// to http://
        ));

        // Fixed typo in OPTIONS and added WebSocket specific headers
        configuration.setAllowedMethods(List.of(
                "HEAD", "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
        ));

        configuration.setAllowCredentials(true);

        // WebSocket specific headers
        configuration.setAllowedHeaders(List.of(
                "Authorization",
                "Cache-Control",
                "Content-Type",
                "correlation_id",
                "Upgrade",          // Needed for WebSocket
                "Connection",       // Needed for WebSocket
                "Sec-WebSocket-Key",
                "Sec-WebSocket-Version",
                "Sec-WebSocket-Extensions"
        ));

        // Add exposed headers if needed
        configuration.setExposedHeaders(List.of(
                "Upgrade",
                "Connection",
                "Sec-WebSocket-Accept"
        ));

        configuration.addExposedHeader("Sec-WebSocket-Accept");
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        log.info("CORS Source configuration completed");
        return source;
    }
}
