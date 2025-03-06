package com.agritechiot.logs.controller;


import com.agritechiot.logs.constant.GenConstant;
import com.agritechiot.logs.dto.ApiResponse;
import com.agritechiot.logs.model.ControlLog;
import com.agritechiot.logs.service.ControlLogService;
import com.agritechiot.logs.util.JsonUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/log")
@RequiredArgsConstructor
@Tag(name = "Control-Logs")
@Slf4j
public class ControlLogController {

    private final ControlLogService controlLogService;

    @PostMapping(value = "/v1/add-control-log")
    public Mono<ApiResponse<?>> addControlLog(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody ControlLog req
    ) throws Exception {
        log.info("REQ_IOT_DEVICE: {}", JsonUtil.toJson(req));
        return controlLogService.saveControlLogs(req)// Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId));
    }

    @PostMapping(value = "/v1/log-by-device-id")
    public Mono<ApiResponse<?>> getListControlLogByDeviceId(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody ControlLog req
    ) throws Exception {
        log.info("REQ_IOT_DEVICE: {}", JsonUtil.toJson(req));
        return controlLogService.getControlLogsByDeviceId(req.getDeviceId())// Collect the Flux into a List
                .collectList()
                .map(res -> new ApiResponse<>(res, correlationId));
    }

    @GetMapping("/v1/control-logs")
    public Mono<ApiResponse<?>> getListControlLog(@RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId) {
        return controlLogService.getControlLogs()
                .collectList()  // Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId));
    }

}
