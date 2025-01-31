package com.agritechiot.agritech_iot.controller;

import com.agritechiot.agritech_iot.constant.GenConstant;
import com.agritechiot.agritech_iot.dto.ApiResponse;
import com.agritechiot.agritech_iot.dto.request.IoTDeviceReq;
import com.agritechiot.agritech_iot.model.IoTDevice;
import com.agritechiot.agritech_iot.service.IoTDeviceService;
import com.agritechiot.agritech_iot.util.JsonUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "IoTDevice")
@Slf4j
public class IoTDeviceController {

    private final IoTDeviceService ioTDeviceService;

    @GetMapping("/v1/iot/devices")
    public Mono<ApiResponse<?>> getListIoTDevices(@RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId) {
        return ioTDeviceService.getListDevice()
                .collectList()  // Collect the Flux into a List
                .map(devices -> new ApiResponse<>(devices, correlationId));
    }

    @PostMapping(value = "/v1/iot/getDeviceName")
    public Mono<ApiResponse<?>> getIoTDevicesByName(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody IoTDeviceReq ioTDeviceReq
    ) throws Exception {
        log.info("REQ_IOT_DEVICE: {}", JsonUtil.toJson(ioTDeviceReq));
        return ioTDeviceService.getDeviceByName(ioTDeviceReq.getName())// Collect the Flux into a List
                .map(devices -> new ApiResponse<>(devices, correlationId));
    }

    @GetMapping
    public Flux<IoTDevice> getAllDevices() {
        return ioTDeviceService.getListDevice();
    }

}
