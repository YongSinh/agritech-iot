package com.agritechiot.iot.controller;

import com.agritechiot.iot.Schedule.SchedulingConfig;
import com.agritechiot.iot.constant.GenConstant;
import com.agritechiot.iot.dto.ApiResponse;
import com.agritechiot.iot.dto.request.RepeatScheduleReq;
import com.agritechiot.iot.dto.request.UpdateScheduleStatusReq;
import com.agritechiot.iot.dto.response.ActiveScheduleRes;
import com.agritechiot.iot.model.RepeatSchedule;
import com.agritechiot.iot.service.LogService;
import com.agritechiot.iot.service.RepeatScheduleService;
import com.agritechiot.iot.util.JsonUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@RestController
@RequestMapping("/iot")
@RequiredArgsConstructor
@Tag(name = "Repeat-Schedule")
@Slf4j
public class RepeatScheduleController {
    private final RepeatScheduleService repeatScheduleService;
    private final SchedulingConfig config;
    private final LogService logService;

    @GetMapping("/v1/repeat-schedule")
    public Mono<ApiResponse<List<RepeatSchedule>>> getListRepeatSchedule(@RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId) {
        logService.logInfo("INIT_LIST_REPEAT_SCHEDULE");
        return repeatScheduleService.getListRepeatSchedule()
                .collectList()  // Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId));
    }

    @PostMapping("/v1/repeat-schedule/device-id")
    public Mono<ApiResponse<List<RepeatSchedule>>> getListRepeatScheduleByDeviceId(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody RepeatScheduleReq req
    ) {
        logService.logInfo("INIT_LIST_REPEAT_SCHEDULE_DEVICE");
        return repeatScheduleService.getListRepeatScheduleByDeviceId(req.getDeviceId())
                .collectList()  // Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId))
                .onErrorResume(Exception.class, ex ->
                        Mono.just(new ApiResponse<>(
                                ex.getMessage(),
                                correlationId,
                                GenConstant.ERR_CODE
                        ))
                );
    }

    @PostMapping("/v1/repeat-schedule/-day")
    public Mono<ApiResponse<List<RepeatSchedule>>> getListRepeatScheduleByDay(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody RepeatScheduleReq req
    ) {
        logService.logInfo("INIT_LIST_REPEAT_SCHEDULE_DAY");
        return repeatScheduleService.getListRepeatScheduleByDay(req.getDay())
                .collectList()  // Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId))
                .onErrorResume(Exception.class, ex ->
                        Mono.just(new ApiResponse<>(
                                ex.getMessage(),
                                correlationId,
                                GenConstant.ERR_CODE
                        ))
                );
    }

    @PostMapping("/v1/repeat-schedule/create")
    public Mono<ApiResponse<RepeatSchedule>> createRepeatSchedule(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody RepeatScheduleReq req
    ) {
        logService.logInfo("INIT_IOT_CREATE_REPEAT_SCHEDULE");
        return repeatScheduleService.saveRepeatSchedule(req)
                .publishOn(Schedulers.boundedElastic())
                .doOnSuccess(updateRepeatSchedule -> config.refreshRepeatScheduledTasksById(req.getId()))
                .map(res -> new ApiResponse<>(res, correlationId));
    }

    @PostMapping("/v1/repeat-schedule/update")
    public Mono<ApiResponse<RepeatSchedule>> updateRepeatSchedule(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody RepeatScheduleReq req
    ) {
        logService.logInfo("INIT_IOT_UPDATE_REPEAT_SCHEDULE");
        return repeatScheduleService.updateRepeatSchedule(req.getId(), req)
                .publishOn(Schedulers.boundedElastic())
                .doOnSuccess(updateRepeatSchedule -> config.refreshRepeatScheduledTasksById(req.getId()))
                .map(res -> new ApiResponse<>(res, correlationId));
    }

    @PostMapping("/v1/repeat-schedule/update-multiple-status")
    public Mono<ApiResponse<Object>> updateMultipleStatus(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody UpdateScheduleStatusReq req
    ) {
        logService.logInfo("INIT_UPDATE_MULTIPLE_STATUS", JsonUtil.toJson(req));
        return repeatScheduleService.updateListsStatus(req.getIds(), req.getStatus(), req.getBatchSize())
                .then(Mono.fromCallable(ApiResponse::new))
                .onErrorResume(Exception.class, ex ->
                        Mono.just(new ApiResponse<>(
                                ex.getMessage(),
                                correlationId,
                                GenConstant.ERR_CODE
                        ))
                );
    }

    @PostMapping("/v1/repeat-schedule/update-single-status")
    public Mono<ApiResponse<Object>> updateSingleStatus(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody UpdateScheduleStatusReq req
    ) {
        logService.logInfo("INIT_UPDATE_SINGLE_STATUS", JsonUtil.toJson(req));
        return repeatScheduleService.updateSingleStatus(req.getId(), req.getStatus())
                .then(Mono.fromCallable(ApiResponse::new))
                .onErrorResume(Exception.class, ex ->
                        Mono.just(new ApiResponse<>(
                                ex.getMessage(),
                                correlationId,
                                GenConstant.ERR_CODE
                        ))
                );
    }

    @GetMapping("/v1/repeat-schedule/check-status-active")
    public Mono<ApiResponse<ActiveScheduleRes>> getActiveRepeatSchedule(@RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId) {
        logService.logInfo("INIT_LIST_REPEAT_SCHEDULE");
        return repeatScheduleService.getActiveRepeatSchedule()// Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId))
                .onErrorResume(Exception.class, ex ->
                        Mono.just(new ApiResponse<>(
                                ex.getMessage(),
                                correlationId,
                                GenConstant.ERR_CODE
                        ))
                );
    }

    @DeleteMapping("/v1/repeat-schedule/{id}")
    public Mono<ApiResponse<Object>> deleteRecord(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @PathVariable Integer id
    ) {
        logService.logInfo("INIT_REPEAT_SCHEDULE_DELETE_RECORD");
        return repeatScheduleService.softDeleteById(id)
                .then(Mono.just(new ApiResponse<>())
                        .onErrorResume(Exception.class, ex ->
                                Mono.just(new ApiResponse<>(
                                        ex.getMessage(),
                                        correlationId,
                                        GenConstant.ERR_CODE
                                ))
                        ));
    }

}
