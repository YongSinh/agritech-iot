package com.agritechiot.agritech_iot.service;

import com.agritechiot.agritech_iot.dto.request.IoTDeviceReq;
import com.agritechiot.agritech_iot.model.IoTDevice;
import com.agritechiot.agritech_iot.repository.IoTDeviceRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
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
    public Mono<IoTDevice> getDeviceById(String id) {
        return ioTDeviceRepo.findById(id);
    }

    @Override
    public Mono<IoTDevice> getDeviceByName(String name) {
        return ioTDeviceRepo.findByName(name);
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
        return ioTDeviceRepo.save(device)
                .onErrorResume(error -> {
                    // Handle specific exceptions, e.g., duplicate key, validation errors, etc.
                    if (error instanceof DuplicateKeyException) {
                        return Mono.error(new RuntimeException("Device with ID '001' already exists."));
                    } else if (error instanceof DataIntegrityViolationException) {
                        return Mono.error(new RuntimeException("Invalid data provided for the device."));
                    } else {
                        return Mono.error(new RuntimeException("Failed to save the device: " + error.getMessage()));
                    }
                });
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
