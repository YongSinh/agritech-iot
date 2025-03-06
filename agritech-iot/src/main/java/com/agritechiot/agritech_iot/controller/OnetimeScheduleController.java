package com.agritechiot.agritech_iot.controller;

import com.agritechiot.agritech_iot.constant.GenConstant;
import com.agritechiot.agritech_iot.dto.ApiResponse;
import com.agritechiot.agritech_iot.service.OnetimeScheduleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/iot/api")
@RequiredArgsConstructor
@Tag(name = "Onetime-Schedule")
@Slf4j
public class OnetimeScheduleController {
    private final OnetimeScheduleService onetimeScheduleService;

    @GetMapping("/v1/one-time-schedules")
    public Mono<ApiResponse<?>> getListOnetimeSchedule(@RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId) {
        return onetimeScheduleService.getListOnetimeSchedule()
                .collectList()  // Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId));
    }
}
