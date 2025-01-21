package com.agritechiot.agritech_iot.service;

import com.agritechiot.agritech_iot.model.IoTDevice;
import reactor.core.publisher.Flux;

public interface IoTDeviceService {
    Flux<IoTDevice> getListDevice();
}
