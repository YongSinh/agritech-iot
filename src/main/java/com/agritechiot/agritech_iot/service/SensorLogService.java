package com.agritechiot.agritech_iot.service;

import com.agritechiot.agritech_iot.model.SensorLog;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SensorLogService {
    Mono<SensorLog> saveSensorLog(SensorLog req);
    Mono<SensorLog> updateSensorLog(String id, SensorLog req);
    Flux<SensorLog> getListSensorLog();
    Flux<SensorLog> getSensorLogByDeviceId(Integer deviceId);

}
