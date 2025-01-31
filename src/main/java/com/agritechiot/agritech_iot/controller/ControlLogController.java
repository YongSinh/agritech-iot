package com.agritechiot.agritech_iot.controller;

import com.agritechiot.agritech_iot.constant.GenConstant;
import com.agritechiot.agritech_iot.dto.ApiResponse;
import com.agritechiot.agritech_iot.dto.request.PaginatedReq;
import com.agritechiot.agritech_iot.model.ControlLog;
import com.agritechiot.agritech_iot.service.ControlLogService;
import com.agritechiot.agritech_iot.util.JsonUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Control-Logs")
@Slf4j
public class ControlLogController {

    private final ControlLogService controlLogService;

    @PostMapping(value = "/v1/iot/add-control-log")
    public Mono<ApiResponse<?>> addControlLog(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody ControlLog req
    ) throws Exception {
        log.info("REQ_IOT_DEVICE: {}", JsonUtil.toJson(req));
        return controlLogService.saveControlLogs(req)// Collect the Flux into a List
                .map(devices -> new ApiResponse<>(devices, correlationId));
    }

    @GetMapping("/v1/iot/control-logs")
    public Mono<ApiResponse<?>> getListControlLog(@RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId) {
        return controlLogService.getControlLogs()
                .collectList()  // Collect the Flux into a List
                .map(controlLogs -> new ApiResponse<>(controlLogs, correlationId));
    }

    @PostMapping("/v1/iot/control-logs-by-page")
    public Mono<ApiResponse<?>> getListControlLogByPage(@RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
                                                        @RequestBody PaginatedReq req
    ) {
        return controlLogService.getPaginatedControlLogs(req.getPage(), req.getSize())
                .collectList()  // Collect the Flux into a List
                .map(controlLogs -> new ApiResponse<>(controlLogs, correlationId));
    }

}
