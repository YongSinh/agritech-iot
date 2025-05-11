package com.agritechiot.iot.controller;


import com.agritechiot.iot.constant.GenConstant;
import com.agritechiot.iot.dto.ApiResponse;
import com.agritechiot.iot.dto.request.ControlLogReq;
import com.agritechiot.iot.model.ControlLog;
import com.agritechiot.iot.service.ControlLogService;
import com.agritechiot.iot.util.JsonUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/iot/api")
@RequiredArgsConstructor
@Tag(name = "Interval-Schedule")
@Slf4j
public class ControlLogController {

    private final ControlLogService controlLogService;

    @PostMapping(value = "/v1/create-control-log")
    public Mono<ApiResponse<ControlLog>> addControlLog(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody ControlLogReq req
    ) {
        log.info("INIT_CREATE_CONTROL_LOG");
        log.info("REQ_CREATE_CONTROL_LOG: {}", JsonUtil.toJson(req));
        return controlLogService.saveControlLog(req)// Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId));
    }

    @PostMapping(value = "/v1/update-control-log")
    public Mono<ApiResponse<ControlLog>> updateControlLog(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody ControlLogReq req
    ) {
        log.info("INIT_UPDATE_CONTROL_LOG");
        log.info("REQ_UPDATE_CONTROL_LOG: {}", JsonUtil.toJson(req));
        return controlLogService.updateControlLog(req)// Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId));
    }

    @GetMapping("/v1/control-logs")
    public Mono<ApiResponse<List<ControlLog>>> getListControlLog(@RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId) {
        log.info("INIT_LIST_CONTROL_LOG");
        return controlLogService.getControlLogs()
                .collectList()  // Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId));
    }

    @GetMapping("/v1/control-logs/filter")
    public Mono<ApiResponse<List<ControlLog>>> getFilteredControlLogs(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody ControlLogReq req
    ) {
        log.info("INIT_FILTERED_CONTROL_LOGS");
        return controlLogService.getControlLogs()
                .collectList()  // Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId));
    }

}
