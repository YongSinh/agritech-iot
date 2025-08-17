package com.agritechiot.iot.service;

import com.agritechiot.iot.dto.request.ControlLogReq;
import com.agritechiot.iot.dto.request.DeviceCommandReq;
import com.agritechiot.iot.model.ControlLog;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ControlLogService {
    Mono<ControlLog> saveControlLog(ControlLogReq req);

    Mono<ControlLog> updateControlLog(ControlLogReq req);

    Flux<ControlLog> getControlLogs();

    Mono<Void> offAndOnControlLogDeviceId(String deviceId, boolean status);

    Mono<Void> offAndOnControlLog(Integer id, boolean stats);

    Mono<Void> sendTaskToDevice(DeviceCommandReq req);

    Mono<Void> scheduledTaskToDevice(DeviceCommandReq req);

    Mono<Void> softDeleteById(Integer id);

    Mono<Void> sendDeviceCommandCheck(DeviceCommandReq req);

    Mono<Void> sendDeviceCommand(DeviceCommandReq req);

}

