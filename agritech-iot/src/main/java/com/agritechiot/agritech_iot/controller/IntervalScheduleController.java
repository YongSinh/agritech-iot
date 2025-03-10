package com.agritechiot.agritech_iot.controller;

import com.agritechiot.agritech_iot.constant.GenConstant;
import com.agritechiot.agritech_iot.dto.ApiResponse;
import com.agritechiot.agritech_iot.dto.request.IntervalScheduleReq;
import com.agritechiot.agritech_iot.model.IntervalSchedule;
import com.agritechiot.agritech_iot.service.IntervalScheduleService;
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
@Tag(name = "Interval-Schedule")
@Slf4j
public class IntervalScheduleController {
    private final IntervalScheduleService intervalScheduleService;

    @PostMapping(value = "/v1/create-interval-schedule")
    public Mono<ApiResponse<IntervalSchedule>> addIntervalRecord(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody IntervalSchedule req
    ) throws Exception {
        log.info("REQ_INTERVAL_SCHEDULE_SAVE: {}", JsonUtil.toJson(req));
        return intervalScheduleService.saveIntervalRecord(req)// Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId));
    }

    @PostMapping(value = "/v1/update-interval-schedule")
    public Mono<ApiResponse<IntervalSchedule>> updateIntervalRecord(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody IntervalSchedule req
    ) throws Exception {
        log.info("REQ_INTERVAL_SCHEDULE_UPDATE: {}", JsonUtil.toJson(req));
        return intervalScheduleService.updateIntervalRecord(req.getId(), req)// Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId));
    }

    @GetMapping("/v1/interval-schedule")
    public Mono<ApiResponse<List<IntervalSchedule>>> getListIntervalSchedule(@RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId) {
        log.info("fetching interval schedule list");
        return intervalScheduleService.getListIntervalRecord()
                .collectList()  // Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId));
    }

    @PostMapping("/v1/interval-schedule-by-device")
    public Mono<ApiResponse<List<IntervalSchedule>>> getIntervalScheduleByDevice(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody IntervalScheduleReq req
    ) throws Exception {
        log.info("REQ_INTERVAL_SCHEDULE: {}", JsonUtil.toJson(req));
        return intervalScheduleService.getListIntervalRecordByDeviceId(req.getDeviceId())
                .collectList()  // Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId));
    }
}