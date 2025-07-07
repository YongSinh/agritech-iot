package com.agritechiot.logs.service;


import com.agritechiot.logs.constant.Fields;
import com.agritechiot.logs.constant.GenConstant;
import com.agritechiot.logs.dto.MqttMessageRes;
import com.agritechiot.logs.model.SensorLog;
import com.agritechiot.logs.repository.SensorLogRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;
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
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Invalid sensor data")))
                .doOnNext(r -> log.debug("Validated MQTT message for device: {}", r.getDeviceId()));
    }

    private Mono<SensorLog> convertToSensorLog(MqttMessageRes req) {
        return Mono.fromCallable(() -> {
            Map<String, Object> measurements = new HashMap<>();
            if (GenConstant.WATER_FLOW_STATUS.equals(req.getStatus())) {
                measurements.put(Fields.FLOW_RATE, req.getFlowRate());
                measurements.put(Fields.FLOW_QUANTITY,req.getFlowQuantity());
                measurements.put(Fields.TOTAL_WATER, req.getTotalWater());
            } else {
                measurements.put("valve_status", req.getValue());
            }

            return SensorLog.builder()
                    .deviceId(req.getDeviceId())
                    .dateTime(LocalDateTime.now())
                    .status(req.getStatus())
                    .value(req.getValue())
                    .measurements(measurements)
                    .build();
        });
    }
}
