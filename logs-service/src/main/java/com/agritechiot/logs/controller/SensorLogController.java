package com.agritechiot.logs.controller;


import com.agritechiot.logs.constant.GenConstant;
import com.agritechiot.logs.dto.ApiResponse;
import com.agritechiot.logs.model.SensorLog;
import com.agritechiot.logs.service.SensorLogService;
import com.agritechiot.logs.util.JsonUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/log")
@RequiredArgsConstructor
@Tag(name = "Sensor-Log")
@Slf4j
public class SensorLogController {
    private final SensorLogService sensorLogService;

    @GetMapping("/v1/sensor-logs")
    public Mono<ApiResponse<?>> getListSensorLog(@RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId) {
        return sensorLogService.getListSensorLog()
                .collectList()  // Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId));
    }

    @PostMapping(value = "/v1/add-sensor-log")
    public Mono<ApiResponse<?>> addSensorLog(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody SensorLog req
    ) throws Exception {
        log.info("REQ_SENSOR_LOG: {}", JsonUtil.toJson(req));
        return sensorLogService.saveSensorLog(req)// Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId));
    }

    @PostMapping(value = "/v1/update-sensor-log")
    public Mono<ApiResponse<?>> updateSensorLog(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody SensorLog req
    ) throws Exception {
        log.info("REQ_SENSOR_LOG: {}", JsonUtil.toJson(req));
        return sensorLogService.updateSensorLog(req.getId(), req)// Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId));
    }

}
