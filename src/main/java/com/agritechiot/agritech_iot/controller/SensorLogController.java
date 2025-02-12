package com.agritechiot.agritech_iot.controller;

import com.agritechiot.agritech_iot.constant.GenConstant;
import com.agritechiot.agritech_iot.dto.ApiResponse;
import com.agritechiot.agritech_iot.service.SensorLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Sensor-Log")
@Slf4j
public class SensorLogController {
    private final SensorLogService sensorLogService;

    @GetMapping("/v1/iot/sensor-logs")
    public Mono<ApiResponse<?>> getListSensorLog(@RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId) {
        return sensorLogService.getListSensorLog()
                .collectList()  // Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId));
    }
}
