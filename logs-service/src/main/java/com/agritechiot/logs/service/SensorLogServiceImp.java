package com.agritechiot.logs.service;


import com.agritechiot.logs.constant.Fields;
import com.agritechiot.logs.model.SensorLog;
import com.agritechiot.logs.repository.SensorLogRepo;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SensorLogServiceImp implements SensorLogService {
    private final SensorLogRepo sensorLogRepo;

    @Override
    public Mono<SensorLog> saveSensorLog(SensorLog req) {
        SensorLog sensorLog = new SensorLog();
        sensorLog.setDeviceId(req.getDeviceId());
        sensorLog.setDateTime(req.getDateTime());
        sensorLog.setAction(req.getAction());
        sensorLog.setValue(req.getValue());
        return sensorLogRepo.save(sensorLog);
    }

    @Override
    public Mono<SensorLog> updateSensorLog(String id, SensorLog req) {
        return sensorLogRepo.findById(id)
                .switchIfEmpty(Mono.error(new Exception("ONE_TIME_SCHEDULE_NOT_FOUND")))
                .map(sensorLog -> {
                    sensorLog.setId(id);
                    sensorLog.setDeviceId(req.getDeviceId());
                    sensorLog.setDateTime(req.getDateTime());
                    sensorLog.setAction(req.getAction());
                    sensorLog.setValue(req.getValue());
                    return sensorLog;
                }).flatMap(sensorLogRepo::save);
    }

    @Override
    public Flux<SensorLog> getListSensorLog() {
        return sensorLogRepo.findAll();
    }

    @Override
    public Flux<SensorLog> getSensorLogByDeviceId(String deviceId) {
        return sensorLogRepo.findByDeviceId(deviceId);
    }

    @Override
    public Mono<SensorLog> saveSensorLog(JsonNode req) {
        return validateFields(req)
                .flatMap(validReq -> {
                    SensorLog sensorLog = new SensorLog();
                    sensorLog.setDeviceId(validReq.path(Fields.DEVICE_ID).asText());
                    sensorLog.setDateTime(LocalDateTime.now());
                    sensorLog.setAction(validReq.path(Fields.ACTION).asText());
                    sensorLog.setValue(validReq.path(Fields.VALUE).doubleValue());
                    return sensorLogRepo.save(sensorLog);
                });
    }

    private Mono<JsonNode> validateFields(JsonNode req) {
        if (req.path(Fields.DEVICE_ID).isMissingNode() || req.path(Fields.DEVICE_ID).isNull()
                || req.path(Fields.ACTION).isMissingNode() || req.path(Fields.ACTION).isNull()
        || req.path(Fields.VALUE).isMissingNode() || req.path(Fields.VALUE).isNull()){
            return Mono.empty(); // validation failed
        }
        return Mono.just(req); // validation passed
    }

}
