package com.agritechiot.iot.controller;

import com.agritechiot.iot.constant.GenConstant;
import com.agritechiot.iot.dto.ApiResponse;
import com.agritechiot.iot.dto.request.MqttTopicReq;
import com.agritechiot.iot.model.MqttTopic;
import com.agritechiot.iot.service.LogService;
import com.agritechiot.iot.service.mqtt.MqttTopicService;
import com.agritechiot.iot.service.mqtt.Publisher;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/iot")
@RequiredArgsConstructor
@Tag(name = "MQTT")
public class MqttController {
    private final Publisher publisher;
    private final LogService logService;
    private final MqttTopicService mqttTopicService;

    @GetMapping("/v1/mqtt/master-topic")
    public Mono<ApiResponse<List<String>>> getSensors(@RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId) {
        logService.logInfo("GET_TOPIC_MASTER");
        return publisher.getTopicMaster()
                .collectList()  // Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId));
    }

    @GetMapping("/v1/mqtt/topic")
    public Mono<ApiResponse<List<MqttTopic>>> getListTopic(@RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId) {
        logService.logInfo("INIT_LIST_MQTT_TOPIC");
        return mqttTopicService.findAll()
                .collectList()  // Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId));
    }

    @PostMapping(value = "/v1/mqtt/add")
    public Mono<ApiResponse<MqttTopic>> addMqttTopic(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody MqttTopicReq req
    ) {
        logService.logInfo("INIT_ADD_MQTT_TOPIC");
        return mqttTopicService.save(req)// Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId))
                .onErrorResume(Exception.class, ex ->
                        Mono.just(new ApiResponse<>(
                                ex.getMessage(),
                                correlationId,
                                GenConstant.ERR_CODE
                        ))
                );
    }

    @PostMapping(value = "/v1/mqtt/update")
    public Mono<ApiResponse<MqttTopic>> updateMqttTopic(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody MqttTopicReq req
    ) {
        logService.logInfo("INIT_UPDATE_MQTT_TOPIC");
        return mqttTopicService.updateById(req.getId(), req)// Collect the Flux into a List
                .map(res -> new ApiResponse<>(res, correlationId))
                .onErrorResume(Exception.class, ex ->
                        Mono.just(new ApiResponse<>(
                                ex.getMessage(),
                                correlationId,
                                GenConstant.ERR_CODE
                        ))
                );
    }

    @DeleteMapping("/v1/mqtt/delete/{id}")
    public Mono<ApiResponse<Object>> deleteMqttTopic(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @PathVariable Integer id
    ) {
        logService.logInfo("INIT_DELETE_MQTT_TOPIC");
        return mqttTopicService.deleteById(id)
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
