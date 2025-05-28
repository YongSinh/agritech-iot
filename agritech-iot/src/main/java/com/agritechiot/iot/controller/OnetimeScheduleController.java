package com.agritechiot.iot.controller;

import com.agritechiot.iot.Schedule.SchedulingConfig;
import com.agritechiot.iot.constant.GenConstant;
import com.agritechiot.iot.dto.ApiResponse;
import com.agritechiot.iot.dto.request.OnetimeScheduleReq;
import com.agritechiot.iot.dto.request.UpdateScheduleStatusReq;
import com.agritechiot.iot.dto.response.ActiveScheduleRes;
import com.agritechiot.iot.model.OnetimeSchedule;
import com.agritechiot.iot.service.LogService;
import com.agritechiot.iot.service.OnetimeScheduleService;
import com.agritechiot.iot.util.ErrorHandlerUtil;
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
@Tag(name = "Onetime-Schedule")
@Slf4j
public class OnetimeScheduleController {
    private final OnetimeScheduleService onetimeScheduleService;
    private final SchedulingConfig config;
    private final LogService logService;

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
                .publishOn(Schedulers.boundedElastic())
                .doOnSuccess(updateRepeatSchedule -> config.refreshOnetimeScheduledTasksById(req.getId()))
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
                .publishOn(Schedulers.boundedElastic())
                .doOnSuccess(updateRepeatSchedule -> config.refreshOnetimeScheduledTasksById(req.getId()))
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


    @PostMapping("/v1/onetime-Schedule/update-multiple-status")
    public Mono<ApiResponse<Object>> updateMultipleStatus(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody UpdateScheduleStatusReq req
    ) {
        logService.logInfo("INIT_ONETIME_SCHEDULE_UPDATE_MULTIPLE_STATUS", JsonUtil.toJson(req));
        return onetimeScheduleService.updateListsStatus(req.getIds(), req.getStatus(), req.getBatchSize())
                .then(Mono.fromCallable(ApiResponse::new))
                .onErrorResume(e -> {
                    log.error("Error updating schedules for deviceId : {}", e.getCause().getMessage(), e);
                    return Mono.just(new ApiResponse<>(List.of(), correlationId)); // Return empty list or custom error response
                });
    }

    @PostMapping("/v1/onetime-Schedule/update-single-status")
    public Mono<ApiResponse<Object>> updateSingleStatus(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody UpdateScheduleStatusReq req
    ) {
        logService.logInfo("INIT_ONETIME_SCHEDULE_UPDATE_SINGLE_STATUS", JsonUtil.toJson(req));
        return onetimeScheduleService.updateSingleStatus(req.getId(), req.getStatus())
                .then(Mono.fromCallable(ApiResponse::new))
                .onErrorResume(e -> {
                    log.error("Error updating schedules: {}", e.getCause().getMessage(), e);
                    return Mono.just(new ApiResponse<>(List.of(), correlationId)); // Return empty list or custom error response
                });
    }

    @GetMapping("/v1/one-time-schedules/check-status-active")
    public Mono<ApiResponse<ActiveScheduleRes>> getActiveOnetimeSchedule(@RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId) {
        return onetimeScheduleService.getActiveOnetimeSchedule()
                .map(res -> new ApiResponse<>(res, correlationId))
                .onErrorResume(e -> {
                    log.error("Error fetching schedules", e);
                    return Mono.just(new ApiResponse<>()); // Return empty list on error
                });
    }

    @DeleteMapping("/v1/one-time-schedules/{id}")
    public Mono<ApiResponse<Object>> deleteRecord(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @PathVariable Integer id
    ) {
        logService.logInfo("INIT_ONETIME_SCHEDULE_DELETE_RECORD");
        return onetimeScheduleService.softDeleteById(id)
                .then(Mono.just(new ApiResponse<>())
                        .onErrorResume(e -> {
                            log.error("Error deleting control log with ID: {}", id, e);
                            return Mono.just(new ApiResponse<>()); // Return empty list on error
                        }));
    }
}
