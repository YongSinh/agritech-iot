package com.agritechiot.agritech_iot.service;

import com.agritechiot.agritech_iot.repository.SensorLogRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SensorLogServiceImp implements SensorLogService {
    private final SensorLogRepo sensorLogRepo;
}
