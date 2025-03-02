package com.agritechiot.agritech_iot.service;

import com.agritechiot.agritech_iot.model.IoTDevice;
import com.agritechiot.agritech_iot.repository.IoTDeviceRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class IoTDeviceServiceImp implements IoTDeviceService {
    private final IoTDeviceRepo ioTDeviceRepo;

    @Override
    public Flux<IoTDevice> getListDevice() {
        return ioTDeviceRepo.findAll();
    }
}
