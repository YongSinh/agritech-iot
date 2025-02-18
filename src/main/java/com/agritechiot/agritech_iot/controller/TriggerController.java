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

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Trigger")
@Slf4j
public class TriggerController {
    private final TriggerService triggerService;

    @GetMapping("/v1/iot/triggers")
    public Mono<ApiResponse<?>> getListSensorLog(@RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId) {
        return triggerService.getTriggers()
                .collectList()  // Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId));
    }

    @PostMapping(value = "/v1/iot/add-trigger")
    public Mono<ApiResponse<?>> addTrigger(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody Trigger req
    ) throws Exception {
        log.info("REQ_TRIGGER: {}", JsonUtil.toJson(req));
        return triggerService.saveTrigger(req)// Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId));
    }
}
