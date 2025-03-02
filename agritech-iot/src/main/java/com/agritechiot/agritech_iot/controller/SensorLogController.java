package com.agritechiot.agritech_iot.controller;

import com.agritechiot.agritech_iot.constant.GenConstant;
import com.agritechiot.agritech_iot.dto.ApiResponse;
import com.agritechiot.agritech_iot.model.SensorLog;
import com.agritechiot.agritech_iot.service.SensorLogService;
import com.agritechiot.agritech_iot.util.JsonUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping(value = "/v1/iot/add-sensor-log")
    public Mono<ApiResponse<?>> addSensorLog(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody SensorLog req
    ) throws Exception {
        log.info("REQ_SENSOR_LOG: {}", JsonUtil.toJson(req));
        return sensorLogService.saveSensorLog(req)// Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId));
    }

    @PostMapping(value = "/v1/iot/update-sensor-log")
    public Mono<ApiResponse<?>> updateSensorLog(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody SensorLog req
    ) throws Exception {
        log.info("REQ_SENSOR_LOG: {}", JsonUtil.toJson(req));
        return sensorLogService.updateSensorLog(req.getId(), req)// Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId));
    }

    @PostMapping(value = "/v1/iot/sensor-log-by-device")
    public Mono<ApiResponse<?>> getSensorLogByDevice(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody SensorLog req
    ) throws Exception {
        log.info("REQ_SENSOR_LOG: {}", JsonUtil.toJson(req));
        return sensorLogService.getSensorLogByDeviceId(req.getDeviceId())
                .collectList()// Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId));
    }

}
