package com.agritechiot.agritech_iot.service;

import com.agritechiot.agritech_iot.dto.request.IoTDeviceReq;
import com.agritechiot.agritech_iot.dto.response.IoTDeviceDto;
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
    public Flux<IoTDeviceDto> getListDevice() {
        return ioTDeviceRepo.findAll()
                .map(IoTDevice::toDto); // ✅ Convert each entity to DTO
    }

    @Override
    public Mono<IoTDevice> getDeviceById(String id) {
        return ioTDeviceRepo.findById(id);
    }

    @Override
    public Flux<IoTDeviceDto> getDeviceByName(String name) {
        return ioTDeviceRepo.findByName(name)
                .map(IoTDevice::toDto);
    }

    @Override
    public Mono<IoTDevice> saveDevice(IoTDeviceReq req) {
        IoTDevice device = IoTDevice.builder()
                .deviceId(req.getDeviceId()) // Ensure this ID is unique or let the repository generate it
                .name(req.getName())
                .sensors(req.getSensors())
                .remark(req.getRemark())
                .controller(req.getController())
                .isNewEntry(true) // Mark as a new entry
                .build();
        return ioTDeviceRepo.save(device);
    }

    @Override
    public Mono<IoTDevice> updateDevice(String id, IoTDeviceReq req) {
        return ioTDeviceRepo.findById(id)
                .switchIfEmpty(Mono.error(new Exception("IOT_DEVICE_NOT_FOUND")))
                .map(d -> {
                    d.setDeviceId(id);
                    d.setName(req.getName());
                    d.setRemark(req.getRemark());
                    d.setSensors(req.getSensors());
                    d.setController(req.getController());
                    return d;
                }).flatMap(ioTDeviceRepo::save);
    }


}
