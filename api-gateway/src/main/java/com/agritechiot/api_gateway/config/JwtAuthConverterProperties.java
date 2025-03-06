package com.agritechiot.api_gateway.config;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@Configuration
@ConfigurationProperties(prefix = "jwt.auth.converter")
@RequiredArgsConstructor
public class JwtAuthConverterProperties {
    private String resourceId;
    private String principalAttribute;
}
