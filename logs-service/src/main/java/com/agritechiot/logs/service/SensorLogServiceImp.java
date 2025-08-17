package com.agritechiot.logs.service;


import com.agritechiot.logs.dto.FilterReq;
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
    public Flux<SensorLog> getSensorLogFilter(FilterReq req) {
        Flux<SensorLog> logs;
        if (req.getDeviceId() != null) {
            logs = sensorLogRepo.findByDeviceId(req.getDeviceId());
        } else if (req.getStatus() != null) {
            logs = sensorLogRepo.findByStatus(req.getStatus());
        } else if (req.getTopic() != null) {
            logs = sensorLogRepo.findByFromTopic(req.getTopic()).take(req.getLimit());
        }
        else {
            logs = Flux.empty();
        }
        return logs;
    }

    @Override
    public Mono<SensorLog> saveSensorLog(Object req, String fromTopic) {
        SensorLog sensorLog = new SensorLog();
        sensorLog.setDateTime(LocalDateTime.now());
        sensorLog.setData(req);
        sensorLog.setFromTopic(fromTopic);
        return sensorLogRepo.save(sensorLog);
    }


}
