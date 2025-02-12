package com.agritechiot.agritech_iot.service;

import com.agritechiot.agritech_iot.dto.request.IoTDeviceReq;
import com.agritechiot.agritech_iot.model.IoTDevice;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IoTDeviceService {
    Flux<IoTDevice> getListDevice();

    Mono<IoTDevice> getDeviceById(Integer id);

    Mono<IoTDevice> getDeviceByName(String name);

    Mono<IoTDevice> saveDevice(IoTDeviceReq req);

    Mono<IoTDevice> updateDevice(Integer id, IoTDeviceReq req);

}
