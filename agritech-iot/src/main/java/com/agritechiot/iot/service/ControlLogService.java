package com.agritechiot.iot.service;

import com.agritechiot.iot.dto.request.ControlLogReq;
import com.agritechiot.iot.model.ControlLog;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ControlLogService {
    Mono<ControlLog> saveControlLog(ControlLogReq req);

    Mono<ControlLog> updateControlLog(ControlLogReq req);

    Flux<ControlLog> getControlLogs();

    Mono<ControlLog> offAndOnControlLog(ControlLogReq req);

    Flux<ControlLog> getControlLogsWithFilters(ControlLogReq req);

    Mono<Void> sendTaskToDevice(Integer id) throws Exception;

    Mono<Void> softDeleteById(Integer id);
}

