package com.agritechiot.logs.service;


import com.agritechiot.logs.model.SensorLog;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SensorLogService {

    Flux<SensorLog> getListSensorLog();

    Flux<SensorLog> getSensorLogByDeviceId(String deviceId);

    Flux<SensorLog> getSensorLogByStatus(String status);

    Mono<SensorLog> saveSensorLog(Object req);


}
