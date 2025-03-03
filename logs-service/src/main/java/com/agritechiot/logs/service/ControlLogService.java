package com.agritechiot.logs.service;


import com.agritechiot.logs.model.ControlLog;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ControlLogService {
    Flux<ControlLog> getPaginatedControlLogs(int page, int size);

    Flux<ControlLog> getControlLogs();

    Mono<ControlLog> saveControlLogs(ControlLog req);

    Mono<ControlLog> updateControlLogs(Integer id, ControlLog req);

    Flux<ControlLog> getControlLogsByDeviceId(String id);

    Mono<ControlLog> getControlLog(Integer id);

    Flux<ControlLog> getControlLogsByDuration(Integer duration, Integer duration2);
}
