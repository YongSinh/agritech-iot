package com.agritechiot.logs.service;


import com.agritechiot.logs.dto.FilterReq;
import com.agritechiot.logs.model.SensorLog;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SensorLogService {

    Flux<SensorLog> getListSensorLog();

    Flux<SensorLog> getSensorLogFilter(FilterReq req);

    Mono<SensorLog> saveSensorLog(Object req, String topic);


}
