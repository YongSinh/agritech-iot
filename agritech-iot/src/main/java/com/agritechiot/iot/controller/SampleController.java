package com.agritechiot.iot.controller;

import com.agritechiot.iot.config.Mqtt;
import com.agritechiot.iot.constant.GenConstant;
import com.agritechiot.iot.dto.ApiResponse;
import com.agritechiot.iot.dto.request.MqttPublishReq;
import com.agritechiot.iot.model.Trigger;
import com.agritechiot.iot.repository.TriggerRepo;
import com.agritechiot.iot.service.integration.LogClient;
import com.agritechiot.iot.service.mqtt.Publisher;
import com.agritechiot.iot.util.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/iot/v1")
@RequiredArgsConstructor
@Slf4j
public class SampleController {
    private final SimpMessagingTemplate messagingTemplate;
    private final TriggerRepo triggerRepo;
    private final Publisher publisher;
    private final Mqtt mqtt;
    private final LogClient logClient;
    @Value("${spring.profiles.active}")
    private String activeProfile;

    @PostMapping("/sample")
    public ResponseEntity<Object> sample(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody MqttPublishReq req) throws MqttException {
        Trigger res = triggerRepo.findByDeviceId("008").block();
        log.info("Snake case res: {}", JsonUtil.toJsonSnakeCase(res));
        publisher.publish(req.getTopic(), JsonUtil.toJsonSnakeCase(res), req.getQos(), req.getRetained());
        return ResponseEntity.ok(new ApiResponse<>(req));
    }

    @GetMapping("/sample2")
    public ResponseEntity<JsonNode> sample2() throws Exception {
        log.info("CALL_WEB_CLIENT: {}", logClient.checkInventory());
        return ResponseEntity.ok(JsonUtil.parseJson(logClient.checkInventory()));
    }

    @PostMapping("/send/genMessage")
    public void sendGenMessage(@RequestBody Object message) {
        messagingTemplate.convertAndSend("/topic/public", message);
    }

    @GetMapping("/profile")
    public String getActiveProfile() {
        return "Active Profile: " + activeProfile;
    }


}
