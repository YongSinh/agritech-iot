package com.agritechiot.iot.controller;

import com.agritechiot.iot.constant.GenConstant;
import com.agritechiot.iot.dto.ApiResponse;
import com.agritechiot.iot.service.LogService;
import com.agritechiot.iot.service.mqtt.Publisher;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/iot")
@RequiredArgsConstructor
@Tag(name = "MQTT")
public class MqttController {
    private final Publisher publisher;
    private final LogService logService;

    @GetMapping("/v1/mqtt/master-topic")
    public Mono<ApiResponse<List<String>>> getSensors(@RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId) {
        logService.logInfo("GET_TOPIC_MASTER");
        return publisher.getTopicMaster()
                .collectList()  // Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId));
    }

}
