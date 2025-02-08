package com.agritechiot.agritech_iot.controller;

import com.agritechiot.agritech_iot.service.SensorLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Sensor-Log")
@Slf4j
public class SensorLogController {
    private final SensorLogService sensorLogService;
}
