package com.agritechiot.iot.controller;

import com.agritechiot.iot.constant.GenConstant;
import com.agritechiot.iot.dto.ApiResponse;
import com.agritechiot.iot.dto.request.TriggerReq;
import com.agritechiot.iot.model.Trigger;
import com.agritechiot.iot.service.LogService;
import com.agritechiot.iot.service.TriggerService;
import com.agritechiot.iot.util.JsonUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/iot")
@RequiredArgsConstructor
@Tag(name = "Trigger")
@Slf4j
public class TriggerController {
    private final TriggerService triggerService;
    private final LogService logService;

    @GetMapping("/v1/triggers")
    public Mono<ApiResponse<List<Trigger>>> getListTrigger(@RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId) {
        log.info("fetching trigger list");
        return triggerService.getTriggers()
                .collectList()  // Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId));
    }

    @GetMapping("/v1/triggers/sensors")
    public Mono<ApiResponse<List<String>>> getSensors(@RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId) {
        log.info("fetching trigger list sensors");
        return triggerService.getSensors()
                .collectList()  // Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId));
    }

    @PostMapping("/v1/triggers/get-by-sensor-device")
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

    @PostMapping(value = "/v1/triggers/multiple-create")
    public Flux<ApiResponse<Trigger>> createMultipleTrigger(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody TriggerReq req
    ) {
        logService.logInfo("CREATING_MULTIPLE_TRIGGER");
        return triggerService.saveMultipleTriggers(req)// Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId));
    }

    @PostMapping(value = "/v1/triggers/create")
    public Mono<ApiResponse<Trigger>> createTrigger(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody Trigger req
    ) {
        log.info("REQ_CREATE_TRIGGER: {}", JsonUtil.toJson(req));
        return triggerService.saveTrigger(req)// Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId));
    }

    @PostMapping(value = "/v1/triggers/update")
    public Mono<ApiResponse<Trigger>> updateTrigger(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody Trigger req
    ) throws Exception {
        log.info("REQ_UPDATE_TRIGGER: {}", JsonUtil.toJson(req));
        return triggerService.updateTrigger(req.getId(), req)// Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId));
    }

    @DeleteMapping("/v1/trigger/{id}")
    public Mono<ApiResponse<Object>> deleteRecord(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @PathVariable Integer id
    ) {
        logService.logInfo("INIT_IOT_DEVICE_DELETE_RECORD");
        return triggerService.softDeleteById(id)
                .then(Mono.just(new ApiResponse<>())
                        .onErrorResume(e -> {
                            log.error("Error deleting control log with ID: {}", id, e);
                            return Mono.just(new ApiResponse<>()); // Return empty list on error
                        }));
    }
}
