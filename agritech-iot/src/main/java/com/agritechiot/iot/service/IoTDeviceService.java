package com.agritechiot.iot.service;

import com.agritechiot.iot.dto.request.IoTDeviceReq;
import com.agritechiot.iot.dto.response.DeviceJoinDto;
import com.agritechiot.iot.dto.response.IoTDeviceDto;
import com.agritechiot.iot.model.IoTDevice;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface IoTDeviceService {
    Flux<IoTDeviceDto> getListDevice();

    Mono<IoTDevice> getDeviceById(String id);

    Flux<IoTDeviceDto> getDeviceByName(String name);

    Mono<IoTDevice> saveDevice(IoTDeviceReq req);

    Mono<IoTDevice> updateDevice(String id, IoTDeviceReq req);

    Flux<Map<String, String>> getAllDeviceIds();

    Flux<DeviceJoinDto> getAllDevices();

    Mono<Void> softDeleteById(String id);
}
