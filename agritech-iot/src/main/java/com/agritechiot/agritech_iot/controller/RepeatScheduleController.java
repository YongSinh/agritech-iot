package com.agritechiot.agritech_iot.controller;

import com.agritechiot.agritech_iot.constant.GenConstant;
import com.agritechiot.agritech_iot.dto.ApiResponse;
import com.agritechiot.agritech_iot.dto.request.RepeatScheduleReq;
import com.agritechiot.agritech_iot.model.RepeatSchedule;
import com.agritechiot.agritech_iot.service.RepeatScheduleService;
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
@Tag(name = "Repeat-Schedule")
@Slf4j
public class RepeatScheduleController {
    private final RepeatScheduleService repeatScheduleService;

    @GetMapping("/v1/repeat-schedule")
    public Mono<ApiResponse<List<RepeatSchedule>>> getListRepeatSchedule(@RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId) {
        return repeatScheduleService.getListRepeatSchedule()
                .collectList()  // Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId));
    }

    @PostMapping("/v1/repeat-schedule-by-device-id")
    public Mono<ApiResponse<List<RepeatSchedule>>> getListRepeatScheduleByDeviceId(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody RepeatScheduleReq req
    ) {
        return repeatScheduleService.getListRepeatScheduleByDeviceId(req.getDeviceId())
                .collectList()  // Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId))
                .onErrorResume(e -> {
                    log.error("Error fetching schedules for deviceId: {}", req.getDeviceId(), e);
                    return Mono.just(new ApiResponse<>(List.of(), correlationId)); // Return empty list or custom error response
                });
    }
    @PostMapping("/v1/create-repeat-schedule")
    public Mono<ApiResponse<RepeatSchedule>> createRepeatSchedule(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody RepeatScheduleReq req
    ) throws Exception {
        log.info("REQ_IOT_CREATE_REPEAT_SCHEDULE: {}", JsonUtil.toJson(req));
        return repeatScheduleService.saveRepeatSchedule(req)
                .map(res -> new ApiResponse<>(res, correlationId));
    }

    @PostMapping("/v1/update-repeat-schedule")
    public Mono<ApiResponse<RepeatSchedule>> updateRepeatSchedule(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody RepeatScheduleReq req
    ) throws Exception {
        log.info("REQ_IOT_UPDATE_REPEAT_SCHEDULE: {}", JsonUtil.toJson(req));
        return repeatScheduleService.updateRepeatSchedule(req.getId(), req)
                .map(res -> new ApiResponse<>(res, correlationId));
    }

}
