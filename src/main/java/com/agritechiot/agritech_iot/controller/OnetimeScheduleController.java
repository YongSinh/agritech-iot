package com.agritechiot.agritech_iot.controller;

import com.agritechiot.agritech_iot.service.OnetimeScheduleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Onetime-Schedule")
@Slf4j
public class OnetimeScheduleController {
    private final OnetimeScheduleService onetimeScheduleService;
}
