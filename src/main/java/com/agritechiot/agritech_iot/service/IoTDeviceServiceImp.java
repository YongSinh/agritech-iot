package com.agritechiot.agritech_iot.service;

import com.agritechiot.agritech_iot.model.IoTDevice;
import com.agritechiot.agritech_iot.repository.IoTDeviceRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class IoTDeviceServiceImp implements IoTDeviceService {
    private final IoTDeviceRepo ioTDeviceRepo;

    @Override
    public Flux<IoTDevice> getListDevice() {
        return ioTDeviceRepo.findAll();
    }

    @Override
    public Mono<IoTDevice> getDeviceById(Integer id) {
        return ioTDeviceRepo.findById(id);
    }

    @Override
    public Mono<IoTDevice> getDeviceByName(String name) {
        return ioTDeviceRepo.findByName(name);
    }

    @Override
    public Mono<IoTDevice> saveDevice(IoTDevice req) {
        return ioTDeviceRepo.save(req);
    }

    @Override
    public Mono<IoTDevice> updateDevice(Integer id, IoTDevice req) {
        return ioTDeviceRepo.findById(id)
                .switchIfEmpty(Mono.error(new Exception("IOT_DEVICE_NOT_FOUND")))
                .map(d -> {
                    d.setDeviceid(id);
                    d.setName(req.getName());
                    d.setRemark(req.getRemark());
                    d.setSensors(req.getSensors());
                    return d;
                }).flatMap(ioTDeviceRepo::save);
    }


}
