package com.agritechiot.logs.controller;


import com.agritechiot.logs.constant.GenConstant;
import com.agritechiot.logs.dto.ApiResponse;
import com.agritechiot.logs.dto.FilterReq;
import com.agritechiot.logs.dto.SensorLogReq;
import com.agritechiot.logs.model.SensorLog;
import com.agritechiot.logs.service.SensorLogService;
import com.agritechiot.logs.service.integration.IotService;
import com.agritechiot.logs.util.JsonUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.net.ssl.SSLException;
import java.util.List;

@RestController
@RequestMapping("/api/log")
@RequiredArgsConstructor
@Tag(name = "Sensor-Log")
@Slf4j
public class SensorLogController {
    private final SensorLogService sensorLogService;
    private final IotService iotService;

    @GetMapping("/v1/sensor-logs")
    public Mono<ApiResponse<List<SensorLog>>> getListSensorLog(@RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId) {
        return sensorLogService.getListSensorLog()
                .collectList()  // Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId));
    }

    @GetMapping("/v1/sensor-logs/topic")
    public Mono<ApiResponse<Object>> getTopic(@RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId) throws SSLException {
        return iotService.getTopic()
                .map(res -> new ApiResponse<>(res, correlationId));
    }

    @PostMapping(value = "/v1/sensor-log/add")
    public Mono<ApiResponse<SensorLog>> addSensorLog(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody SensorLogReq req
    ) throws Exception {
        log.info("REQ_CREATE_SENSOR_LOG: {}", JsonUtil.toJson(req));
        return sensorLogService.saveSensorLog(req.getData(), req.getFromTopic())// Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId));
    }

    @PostMapping("/v1/sensor-logs/filter")
    public Mono<ApiResponse<List<SensorLog>>> getSensorLogs(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody FilterReq req
    ) {
        return sensorLogService.getSensorLogFilter(req)
                .collectList()
                .map(res -> new ApiResponse<>(res, correlationId));
    }

}
