package com.agritechiot.agritech_iot.controller;

import com.agritechiot.agritech_iot.constant.GenConstant;
import com.agritechiot.agritech_iot.dto.ApiResponse;
import com.agritechiot.agritech_iot.service.RepeatScheduleService;
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
@Tag(name = "Repeat-Schedule")
@Slf4j
public class RepeatScheduleController {
    private final RepeatScheduleService repeatScheduleService;

    @GetMapping("/v1/repeat-schedule")
    public Mono<ApiResponse<?>> getListRepeatSchedule(@RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId) {
        return repeatScheduleService.getListRepeatSchedule()
                .collectList()  // Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId));
    }
}
