package com.agritechiot.agritech_iot.controller;

import com.agritechiot.agritech_iot.constant.GenConstant;
import com.agritechiot.agritech_iot.dto.ApiResponse;
import com.agritechiot.agritech_iot.model.IoTDevice;
import com.agritechiot.agritech_iot.service.IoTDeviceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "IoTDevice")
public class IoTDeviceController {

    private final IoTDeviceService ioTDeviceService;

    @GetMapping("/iot/devices")
    public Mono<ApiResponse<?>> getListIoTDevices(@RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId) {
        return ioTDeviceService.getListDevice()
                .collectList()  // Collect the Flux into a List
                .map(devices -> new ApiResponse<>(devices, correlationId));
    }

    @GetMapping
    public Flux<IoTDevice> getAllDevices() {
        return ioTDeviceService.getListDevice();
    }

}
