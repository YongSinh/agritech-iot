package com.agritechiot.iot.controller;


import com.agritechiot.iot.constant.GenConstant;
import com.agritechiot.iot.dto.ApiResponse;
import com.agritechiot.iot.dto.request.ControlLogReq;
import com.agritechiot.iot.model.ControlLog;
import com.agritechiot.iot.service.ControlLogService;
import com.agritechiot.iot.service.LogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/iot")
@RequiredArgsConstructor
@Tag(name = "Control-logs")
@Slf4j
public class ControlLogController {

    private final ControlLogService controlLogService;
    private final LogService logService;

    @PostMapping(value = "/v1/create-control-log")
    public Mono<ApiResponse<ControlLog>> addControlLog(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody ControlLogReq req
    ) {

        logService.logInfo("INIT_CREATE_CONTROL_LOG");
        return controlLogService.saveControlLog(req)// Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId));
    }

    @PostMapping(value = "/v1/update-control-log")
    public Mono<ApiResponse<ControlLog>> updateControlLog(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody ControlLogReq req
    ) {
        logService.logInfo("INIT_UPDATE_CONTROL_LOG");
        return controlLogService.updateControlLog(req)// Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId));
    }

    @GetMapping("/v1/control-logs")
    public Mono<ApiResponse<List<ControlLog>>> getListControlLog(@RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId) {
        logService.logInfo("INIT_LIST_CONTROL_LOG");
        return controlLogService.getControlLogs()
                .collectList()  // Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId));
    }

    @PostMapping("/v1/control-logs/filter")
    public Mono<ApiResponse<List<ControlLog>>> getFilteredControlLogs(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody ControlLogReq req
    ) {
        logService.logInfo("INIT_FILTERED_CONTROL_LOGS");
        return controlLogService.getControlLogs()
                .collectList()  // Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId));
    }

    @DeleteMapping("/v1/control-logs/{id}")
    public Mono<ApiResponse<Object>> deleteRecord(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @PathVariable Integer id
    ) {
        logService.logInfo("INIT_DELETE_RECORD_CONTROL_LOGS");
        return controlLogService.softDeleteById(id)
                .then(Mono.just(new ApiResponse<>())
                        .onErrorResume(e -> {
                            log.error("Error deleting control log with ID: {}", id, e);
                            return Mono.just(new ApiResponse<>()); // Return empty list on error
                        }));
    }

}
