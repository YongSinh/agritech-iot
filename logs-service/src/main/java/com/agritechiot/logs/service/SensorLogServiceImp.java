package com.agritechiot.logs.service;


import com.agritechiot.logs.dto.MqttMessageRes;
import com.agritechiot.logs.model.SensorLog;
import com.agritechiot.logs.repository.SensorLogRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SensorLogServiceImp implements SensorLogService {
    private final SensorLogRepo sensorLogRepo;

    @Override
    public Mono<SensorLog> saveSensorLog(SensorLog req) {
        SensorLog sensorLog = new SensorLog();
        sensorLog.setDeviceId(req.getDeviceId());
        sensorLog.setDateTime(req.getDateTime());
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
    public Mono<SensorLog> saveSensorLog(MqttMessageRes req) {
        return validateMqttMessage(req)
                .flatMap(this::convertToSensorLog)
                .flatMap(sensorLogRepo::save)
                .doOnSuccess(sensorLog -> log.debug("Saved sensor log: {}", sensorLog))
                .doOnError(e -> log.error("Failed to save sensor log", e));
    }


    private Mono<MqttMessageRes> validateMqttMessage(MqttMessageRes req) {
        return Mono.just(req)
                .filter(r -> r.getDeviceId() != null)
                .filter(r -> r.getValveStatus() != null ||
                        (r.getFlowRate() != null || r.getTotalWater() != null))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Invalid sensor data")))
                .doOnNext(r -> log.debug("Validated MQTT message for device: {}", r.getDeviceId()));
    }

    private Mono<SensorLog> convertToSensorLog(MqttMessageRes req) {
        return Mono.fromCallable(() -> {
            String sensorType = req.determineSensorType();

            Map<String, Object> measurements = new HashMap<>();
            if ("water_flow".equals(sensorType)) {
                measurements.put("flow_rate", req.getFlowRate());
                measurements.put("total_water", req.getTotalWater());
            } else {
                measurements.put("valve_status", req.getValveStatus());
            }

            return SensorLog.builder()
                    .deviceId(req.getDeviceId())
                    .dateTime(LocalDateTime.now())
                    .status(sensorType)
                    .valveStatus(req.getValveStatus())
                    .flowRate(req.getFlowRate())
                    .totalWater(req.getTotalWater())
                    .measurements(measurements)
                    .build();
        });
    }
}
