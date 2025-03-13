package com.agritechiot.logs.controller;


import com.agritechiot.logs.constant.GenConstant;
import com.agritechiot.logs.dto.ApiResponse;
import com.agritechiot.logs.model.ControlLog;
import com.agritechiot.logs.service.ControlLogService;
import com.agritechiot.logs.util.JsonUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/log")
@RequiredArgsConstructor
@Tag(name = "Control-Logs")
@Slf4j
public class ControlLogController {

    private final ControlLogService controlLogService;

    @PostMapping(value = "/v1/add-control-log")
    public Mono<ApiResponse<ControlLog>> addControlLog(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody ControlLog req
    ) throws Exception {
        log.info("REQ_IOT_DEVICE: {}", JsonUtil.toJson(req));
        return controlLogService.saveControlLogs(req)// Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId));
    }

    @GetMapping(value = "/v2/log-by-device-id")
    public Mono<ApiResponse<List<ControlLog>>> getListControlLogByDeviceId(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestParam String deviceId // Use @RequestParam instead of @RequestBody
    ) {
        log.info("Fetching logs for deviceId: {}", deviceId);

        return controlLogService.getControlLogsByDeviceId(deviceId)
                .collectList()
                .map(res -> new ApiResponse<>(res, correlationId))
                .onErrorResume(ex -> {
                    log.error("Error fetching control logs for deviceId: {}", deviceId, ex);
                    return Mono.just(new ApiResponse<>(Collections.emptyList(), correlationId));
                });
    }

    @GetMapping("/v1/control-logs")
    public Mono<ApiResponse<List<ControlLog>>> getListControlLog(@RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId) {
        return controlLogService.getControlLogs()
                .collectList()  // Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId));
    }

}
