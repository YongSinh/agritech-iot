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
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "IoT-Devices")
@Slf4j
public class IoTDeviceController {

    private final IoTDeviceService ioTDeviceService;

    @GetMapping("/v1/iot/devices")
    public Mono<ApiResponse<?>> getListIoTDevices(@RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId) {
        return ioTDeviceService.getListDevice()
                .collectList()  // Collect the Flux into a List
                .map(devices -> new ApiResponse<>(devices, correlationId));
    }

    @PostMapping(value = "/v1/iot/get-device-by-name")
    public Mono<ApiResponse<?>> getIoTDevicesByName(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody IoTDeviceReq ioTDeviceReq
    ) throws Exception {
        log.info("REQ_IOT_DEVICE: {}", JsonUtil.toJson(ioTDeviceReq));
        return ioTDeviceService.getDeviceByName(ioTDeviceReq.getName())// Collect the Flux into a List
                .map(devices -> new ApiResponse<>(devices, correlationId));
    }

    @PostMapping(value = "/v1/iot/add-device")
    public Mono<ApiResponse<?>> addDevices(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody IoTDevice req
    ) throws Exception {
        log.info("REQ_IOT_DEVICE: {}", JsonUtil.toJson(req));
        return ioTDeviceService.saveDevice(req)// Collect the Flux into a List
                .map(devices -> new ApiResponse<>(devices, correlationId));
    }

    @PutMapping(value = "/v1/iot/update-device")
    public Mono<ApiResponse<?>> updateDevices(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody IoTDevice req
    ) throws Exception {
        log.info("REQ_IOT_DEVICE: {}", JsonUtil.toJson(req));
        return ioTDeviceService.saveDevice(req)// Collect the Flux into a List
                .map(devices -> new ApiResponse<>(devices, correlationId));
    }


}
