package com.agritechiot.agritech_iot.controller;

import com.agritechiot.agritech_iot.dto.ApiResponse;
import com.agritechiot.agritech_iot.service.IntervalScheduleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Interval-Schedule")
@Slf4j
public class IntervalScheduleController {
    private final IntervalScheduleService intervalScheduleService;
    @GetMapping("/schedule")
    public ResponseEntity<?> schedule() {
        return ResponseEntity.ok(new ApiResponse<>());
    }

}