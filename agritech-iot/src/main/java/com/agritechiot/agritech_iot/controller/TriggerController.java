package com.agritechiot.agritech_iot.controller;

import com.agritechiot.agritech_iot.constant.GenConstant;
import com.agritechiot.agritech_iot.dto.ApiResponse;
import com.agritechiot.agritech_iot.model.Trigger;
import com.agritechiot.agritech_iot.service.TriggerService;
import com.agritechiot.agritech_iot.util.JsonUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/iot/api")
@RequiredArgsConstructor
@Tag(name = "Trigger")
@Slf4j
public class TriggerController {
    private final TriggerService triggerService;

    @GetMapping("/v1/triggers")
    public Mono<ApiResponse<List<Trigger>>> getListSensorLog(@RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId) {
        log.info("fetching trigger list");
        return triggerService.getTriggers()
                .collectList()  // Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId));
    }

    @PostMapping("/v1/get-trigger-by-sensor-device")
    public Mono<ApiResponse<List<Trigger>>> getTriggerBySensorAndDeviceId(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody Trigger req

    ) {
        log.info("fetching trigger for deviceId: {} and {}", req.getSensor(), req.getDeviceId());
        return triggerService.getTriggerBySensorAndDeviceId(req.getSensor(), req.getDeviceId())
                .collectList()  // Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId))
                .onErrorResume(e -> {
                    log.error("Error fetching trigger for deviceId: {} and {}", req.getSensor(), req.getDeviceId(), e);
                    return Mono.just(new ApiResponse<>(List.of(), correlationId)); // Return empty list or custom error response
                });
    }


    @PostMapping(value = "/v1/create-trigger")
    public Mono<ApiResponse<Trigger>> createTrigger(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody Trigger req
    ) throws Exception {
        log.info("REQ_CREATE_TRIGGER: {}", JsonUtil.toJson(req));
        return triggerService.saveTrigger(req)// Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId));
    }

    @PostMapping(value = "/v1/update-trigger")
    public Mono<ApiResponse<Trigger>> updateTrigger(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody Trigger req
    ) throws Exception {
        log.info("REQ_UPDATE_TRIGGER: {}", JsonUtil.toJson(req));
        return triggerService.updateTrigger(req.getId(), req)// Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId));
    }


}
