package com.agritechiot.IoT.controller;

import com.agritechiot.IoT.constant.GenConstant;
import com.agritechiot.IoT.dto.ApiResponse;
import com.agritechiot.IoT.dto.request.OnetimeScheduleReq;
import com.agritechiot.IoT.model.OnetimeSchedule;
import com.agritechiot.IoT.service.OnetimeScheduleService;
import com.agritechiot.IoT.util.ErrorHandlerUtil;
import com.agritechiot.IoT.util.JsonUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/iot/api")
@RequiredArgsConstructor
@Tag(name = "Onetime-Schedule")
@Slf4j
public class OnetimeScheduleController {
    private final OnetimeScheduleService onetimeScheduleService;

    @GetMapping("/v1/one-time-schedules")
    public Mono<ApiResponse<List<OnetimeSchedule>>> getListOnetimeSchedule(@RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId) {
        return onetimeScheduleService.getListOnetimeSchedule()
                .collectList()  // Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId))
                .onErrorResume(e -> {
                    log.error("Error fetching schedules", e);
                    return Mono.just(new ApiResponse<>(List.of(), correlationId)); // Return empty list on error
                });
    }

    @PostMapping(value = "/v1/create-onetime-Schedule")
    public Mono<ApiResponse<OnetimeSchedule>> createSchedule(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody OnetimeScheduleReq req
    ) throws Exception {
        log.info("REQ_IOT_ADD_ONE_TIME_SCHEDULE: {}", JsonUtil.toJson(req));
        return onetimeScheduleService.saveOnetimeSchedule(req)// Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId))
                .onErrorResume(error -> ErrorHandlerUtil.handleDuplicateError(error, "Device with ID '" + req.getDeviceId() + "' already exists.", correlationId, "IoTDevice"));
    }

    @PostMapping(value = "/v1/update-onetime-Schedule")
    public Mono<ApiResponse<OnetimeSchedule>> updateSchedule(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody OnetimeScheduleReq req
    ) throws Exception {
        log.info("REQ_IOT_UPDATE_ONE_TIME_SCHEDULE: {}", JsonUtil.toJson(req));
        return onetimeScheduleService.updateOnetimeSchedule(req.getId(), req)// Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId));
    }

    @PostMapping(value = "/v1/onetime-Schedule-by-device-id")
    public Mono<ApiResponse<List<OnetimeSchedule>>> getListOnetimeScheduleByDeviceId(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody OnetimeScheduleReq req
    ) throws Exception {
        log.info("REQ_IOT_U_ONE_TIME_SCHEDULE: {}", JsonUtil.toJson(req));
        return onetimeScheduleService.getListOnetimeScheduleByDeviceId(req.getDeviceId())
                .collectList()// Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId))
                .onErrorResume(e -> {
                    log.error("Error fetching schedules for deviceId: {}", req.getDeviceId(), e);
                    return Mono.just(new ApiResponse<>(List.of(), correlationId)); // Return empty list or custom error response
                });
    }
}
