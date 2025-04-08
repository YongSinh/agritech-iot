package com.agritechiot.IoT.service;

import com.agritechiot.IoT.dto.request.IoTDeviceReq;
import com.agritechiot.IoT.dto.response.IoTDeviceDto;
import com.agritechiot.IoT.model.IoTDevice;
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
}
