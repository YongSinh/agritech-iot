package com.agritechiot.iot.controller;

import com.agritechiot.iot.constant.GenConstant;
import com.agritechiot.iot.dto.ApiResponse;
import com.agritechiot.iot.dto.request.IoTDeviceReq;
import com.agritechiot.iot.dto.request.MqttPublishReq;
import com.agritechiot.iot.dto.response.DeviceJoinDto;
import com.agritechiot.iot.dto.response.IoTDeviceDto;
import com.agritechiot.iot.model.IoTDevice;
import com.agritechiot.iot.service.IoTDeviceService;
import com.agritechiot.iot.service.LogService;
import com.agritechiot.iot.service.mqtt.Publisher;
import com.agritechiot.iot.util.JsonUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/iot")
@RequiredArgsConstructor
@Tag(name = "IoT-Devices")
@Slf4j
public class IoTDeviceController {

    private final IoTDeviceService ioTDeviceService;

    private final Publisher publisher;
    private final LogService logService;

    @GetMapping("/v1/devices")
    public Mono<ApiResponse<List<IoTDeviceDto>>> getListIoTDevices(@RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId) {
        log.info("INIT_LIST_IOT_DEVICES");
        return ioTDeviceService.getListDevice()
                .collectList()  // Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId));
    }

    @GetMapping("/v2/devices")
    public Mono<ApiResponse<List<DeviceJoinDto>>> getListIoTDevices2(@RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId) {
        return ioTDeviceService.getAllDevices()
                .collectList()  // Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId));
    }


    @GetMapping("/v1/device/ids")
    public Mono<ApiResponse<List<Map<String, String>>>> getAllDeviceIds(@RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId) {
        return ioTDeviceService.getAllDeviceIds()
                .collectList()  // Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId));
    }

    @PostMapping(value = "/v1/device/get-by-name")
    public Mono<ApiResponse<List<IoTDeviceDto>>> getIoTDevicesByName(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody IoTDeviceReq ioTDeviceReq
    ) throws Exception {
        log.info("REQ_IOT_DEVICE: {}", JsonUtil.toJson(ioTDeviceReq));
        return ioTDeviceService.getDeviceByName(ioTDeviceReq.getName())
                .collectList()// Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId));
    }

    @PostMapping(value = "/v1/device/device")
    public Mono<ApiResponse<IoTDevice>> addDevices(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody IoTDeviceReq req
    ) throws Exception {
        log.info("REQ_IOT_ADD_DEVICE: {}", JsonUtil.toJson(req));
        return ioTDeviceService.saveDevice(req)// Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId))
                .onErrorResume(Exception.class, ex ->
                        Mono.just(new ApiResponse<>(
                                ex.getMessage(),
                                correlationId,
                                GenConstant.ERR_CODE
                        ))
                );
    }

    @PostMapping(value = "/v1/device/update")
    public Mono<ApiResponse<IoTDevice>> updateDevices(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody IoTDeviceReq req
    ) throws Exception {
        log.info("REQ_IOT_UPDATE_DEVICE: {}", JsonUtil.toJson(req));
        return ioTDeviceService.updateDevice(req.getDeviceId(), req)// Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId));
    }

    @PostMapping(value = "/v1/device/off-on")
    public Mono<ApiResponse<Object>> offOnDevice(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody MqttPublishReq req
    ) throws Exception {
        publisher.publish(req.getTopic(), JsonUtil.objectToJsonString(req.getMessage()), req.getQos(), req.getRetained());
        return Mono.just(new ApiResponse<>(req.getMessage(), correlationId));
    }

    @DeleteMapping("/v1/device/{id}")
    public Mono<ApiResponse<Object>> deleteRecord(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @PathVariable String id
    ) {
        logService.logInfo("INIT_IOT_DEVICE_DELETE_RECORD");
        return ioTDeviceService.softDeleteById(id)
                .then(Mono.just(new ApiResponse<>())
                        .onErrorResume(Exception.class, ex ->
                                Mono.just(new ApiResponse<>(
                                        ex.getMessage(),
                                        correlationId,
                                        GenConstant.ERR_CODE
                                ))
                        ));
    }
}
