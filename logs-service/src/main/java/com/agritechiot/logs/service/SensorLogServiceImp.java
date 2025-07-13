package com.agritechiot.logs.service;


import com.agritechiot.logs.model.SensorLog;
import com.agritechiot.logs.repository.SensorLogRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class SensorLogServiceImp implements SensorLogService {
    private final SensorLogRepo sensorLogRepo;


    @Override
    public Flux<SensorLog> getListSensorLog() {
        return sensorLogRepo.findAll();
    }

    @Override
    public Flux<SensorLog> getSensorLogByDeviceId(String deviceId) {
        return sensorLogRepo.findByDeviceId(deviceId);
    }

    @Override
    public Flux<SensorLog> getSensorLogByStatus(String status) {
        return sensorLogRepo.findByStatus(status);
    }


    @Override
    public Mono<SensorLog> saveSensorLog(Object req) {
        SensorLog sensorLog = new SensorLog();
        sensorLog.setDateTime(LocalDateTime.now());
        sensorLog.setData(req);
        return sensorLogRepo.save(sensorLog);
    }


}
