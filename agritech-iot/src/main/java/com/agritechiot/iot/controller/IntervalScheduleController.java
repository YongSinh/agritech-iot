package com.agritechiot.iot.controller;

import com.agritechiot.iot.constant.GenConstant;
import com.agritechiot.iot.dto.ApiResponse;
import com.agritechiot.iot.dto.request.IntervalScheduleReq;
import com.agritechiot.iot.dto.request.UpdateScheduleStatusReq;
import com.agritechiot.iot.model.IntervalSchedule;
import com.agritechiot.iot.service.IntervalScheduleService;
import com.agritechiot.iot.service.LogService;
import com.agritechiot.iot.util.JsonUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/iot")
@RequiredArgsConstructor
@Tag(name = "Interval-Schedule")
@Slf4j
public class IntervalScheduleController {
    private final IntervalScheduleService intervalScheduleService;
    private final LogService logService;

    @PostMapping(value = "/v1/interval-schedule/create")
    public Mono<ApiResponse<IntervalSchedule>> addIntervalRecord(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody IntervalSchedule req
    ) {
        log.info("REQ_INTERVAL_SCHEDULE_SAVE: {}", JsonUtil.toJson(req));
        return intervalScheduleService.saveIntervalRecord(req)// Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId));
    }

    @PostMapping(value = "/v1/interval-schedule/update")
    public Mono<ApiResponse<IntervalSchedule>> updateIntervalRecord(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody IntervalSchedule req
    ) {
        log.info("REQ_INTERVAL_SCHEDULE_UPDATE: {}", JsonUtil.toJson(req));
        return intervalScheduleService.updateIntervalRecord(req.getId(), req)
                .map(res -> new ApiResponse<>(res, correlationId)); // Map result to response

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
    ) {
        log.info("REQ_INTERVAL_SCHEDULE: {}", JsonUtil.toJson(req));
        return intervalScheduleService.getListIntervalRecordByDeviceId(req.getDeviceId())
                .collectList()  // Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId));
    }

    @DeleteMapping("/v1/interval-schedule/{id}")
    public Mono<ApiResponse<Object>> deleteRecord(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @PathVariable Integer id
    ) {
        logService.logInfo("INIT_INTERVAL_SCHEDULE_DELETE_RECORD");
        return intervalScheduleService.softDeleteById(id)
                .then(Mono.just(new ApiResponse<>())
                        .onErrorResume(Exception.class, ex ->
                                Mono.just(new ApiResponse<>(
                                        ex.getMessage(),
                                        correlationId,
                                        GenConstant.ERR_CODE
                                ))
                        ));
    }

    @PostMapping("/v1/interval-schedule/update-multiple-status")
    public Mono<ApiResponse<Object>> updateMultipleStatus(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody UpdateScheduleStatusReq req
    ) {
        logService.logInfo("INIT_UPDATE_MULTIPLE_STATUS", JsonUtil.toJson(req));
        return intervalScheduleService.updateListsStatus(req.getIds(), req.getStatus(), req.getBatchSize())
                .then(Mono.fromCallable(ApiResponse::new))
                .onErrorResume(Exception.class, ex ->
                        Mono.just(new ApiResponse<>(
                                ex.getMessage(),
                                correlationId,
                                GenConstant.ERR_CODE
                        ))
                );
    }

    @PostMapping("/v1/interval-schedule/update-single-status")
    public Mono<ApiResponse<Object>> updateSingleStatus(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody UpdateScheduleStatusReq req
    ) {
        logService.logInfo("INIT_UPDATE_SINGLE_STATUS", JsonUtil.toJson(req));
        return intervalScheduleService.updateSingleStatus(req.getId(), req.getStatus())
                .then(Mono.fromCallable(ApiResponse::new))
                .onErrorResume(Exception.class, ex ->
                        Mono.just(new ApiResponse<>(
                                ex.getMessage(),
                                correlationId,
                                GenConstant.ERR_CODE
                        ))
                );
    }
}