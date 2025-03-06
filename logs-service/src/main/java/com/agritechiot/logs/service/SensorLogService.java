package com.agritechiot.logs.service;


import com.agritechiot.logs.model.SensorLog;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SensorLogService {
    Mono<SensorLog> saveSensorLog(SensorLog req);

    Mono<SensorLog> updateSensorLog(String id, SensorLog req);

    Flux<SensorLog> getListSensorLog();

    Flux<SensorLog> getSensorLogByDeviceId(String deviceId);

}
