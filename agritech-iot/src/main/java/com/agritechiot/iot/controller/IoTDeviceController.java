package com.agritechiot.iot.controller;

import com.agritechiot.iot.constant.GenConstant;
import com.agritechiot.iot.dto.ApiResponse;
import com.agritechiot.iot.dto.request.IoTDeviceReq;
import com.agritechiot.iot.dto.request.MqttPublishReq;
import com.agritechiot.iot.dto.response.IoTDeviceDto;
import com.agritechiot.iot.model.IoTDevice;
import com.agritechiot.iot.service.IoTDeviceService;
import com.agritechiot.iot.service.mqtt.Publisher;
import com.agritechiot.iot.util.ErrorHandlerUtil;
import com.agritechiot.iot.util.JsonUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/iot/api")
@RequiredArgsConstructor
@Tag(name = "IoT-Devices")
@Slf4j
public class IoTDeviceController {

    private final IoTDeviceService ioTDeviceService;
    private final Publisher publisher;

    @GetMapping("/v1/devices")
    public Mono<ApiResponse<List<IoTDeviceDto>>> getListIoTDevices(@RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId) {
        return ioTDeviceService.getListDevice()
                .collectList()  // Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId));
    }


    @GetMapping("/v1/device-ids")
    public Mono<ApiResponse<List<Map<String, String>>>> getAllDeviceIds(@RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId) {
        return ioTDeviceService.getAllDeviceIds()
                .collectList()  // Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId));
    }

    
    
    @PostMapping(value = "/v1/get-device-by-name")
    public Mono<ApiResponse<List<IoTDeviceDto>>> getIoTDevicesByName(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody IoTDeviceReq ioTDeviceReq
    ) throws Exception {
        log.info("REQ_IOT_DEVICE: {}", JsonUtil.toJson(ioTDeviceReq));
        return ioTDeviceService.getDeviceByName(ioTDeviceReq.getName())
                .collectList()// Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId));
    }

    @PostMapping(value = "/v1/create-device")
    public Mono<ApiResponse<IoTDevice>> addDevices(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody IoTDeviceReq req
    ) throws Exception {
        log.info("REQ_IOT_ADD_DEVICE: {}", JsonUtil.toJson(req));
        return ioTDeviceService.saveDevice(req)// Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId))
                .onErrorResume(error -> ErrorHandlerUtil.handleDuplicateError(error, "Device with ID '" + req.getDeviceId() + "' already exists.", correlationId, "IoTDevice"));
    }

    @PostMapping(value = "/v1/update-device")
    public Mono<ApiResponse<IoTDevice>> updateDevices(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody IoTDeviceReq req
    ) throws Exception {
        log.info("REQ_IOT_UPDATE_DEVICE: {}", JsonUtil.toJson(req));
        return ioTDeviceService.updateDevice(req.getDeviceId(), req)// Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId));
    }

    @PostMapping(value = "/v1/off-on-device")
    public Mono<ApiResponse<Object>> offOnDevice(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody MqttPublishReq req
    ) throws Exception {
        publisher.publish(req.getTopic(), JsonUtil.objectToJsonString(req.getMessage()), req.getQos(), req.getRetained());
        return Mono.just(new ApiResponse<>(req.getMessage(), correlationId));
    }


}
