package com.agritechiot.logs.service.integration;

import com.agritechiot.logs.config.WebClientConfig;
import com.agritechiot.logs.dto.IotRes;
import com.agritechiot.logs.dto.TopicRes;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.net.ssl.SSLException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class IotService {
    private final WebClientConfig webClientConfig;
    @Value("${iot.service.url}")
    private String iotUrl;

    public Mono<List<TopicRes>> getTopic() throws SSLException {
        return webClientConfig.webClient()
                .get()
                .uri(iotUrl + "/iot/v1/mqtt/topic")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<IotRes<List<TopicRes>>>() {})
                .map(IotRes::getData); // unwrap just the data
    }

}
