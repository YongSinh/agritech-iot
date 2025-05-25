package com.agritechiot.iot.service.integration;

import com.agritechiot.iot.config.WebClientConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class LogClient {
    private final WebClientConfig webClientConfig;

    public String checkInventory() {
        return webClientConfig.webClientBuilder().build()
                .get()
                .uri("http://localhost:8082/api/log/v1/sensor-logs")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
