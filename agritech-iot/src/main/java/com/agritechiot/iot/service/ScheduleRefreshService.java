package com.agritechiot.iot.service;

import com.agritechiot.iot.Schedule.SchedulingConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@EnableScheduling
@Slf4j
public class ScheduleRefreshService {
    private final SchedulingConfig schedulingConfig;

}

