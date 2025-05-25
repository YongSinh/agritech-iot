package com.agritechiot.iot.controller;

import com.agritechiot.iot.Schedule.SchedulingConfig;
import com.agritechiot.iot.config.Mqtt;
import com.agritechiot.iot.constant.GenConstant;
import com.agritechiot.iot.dto.ApiResponse;
import com.agritechiot.iot.dto.request.MqttPublishReq;
import com.agritechiot.iot.model.MqttPublishModel;
import com.agritechiot.iot.model.MqttSubscribeModel;
import com.agritechiot.iot.service.integration.LogClient;
import com.agritechiot.iot.service.mqtt.Publisher;
import com.agritechiot.iot.service.mqtt.SubscriberImp;
import com.agritechiot.iot.util.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;


@RestController
@RequestMapping("/iot/api/v1")
@RequiredArgsConstructor
@Slf4j
public class SampleController {
    private final SimpMessagingTemplate messagingTemplate;
    private final Publisher publisher;
    private final Mqtt mqtt;
    private final LogClient logClient;

    @PostMapping("/sample")
    public ResponseEntity<Object> sample(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId,
            @RequestBody MqttPublishReq req) throws Exception {
        publisher.publish(req.getTopic(), JsonUtil.objectToJsonString(req.getMessage()), req.getQos(), req.getRetained());
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

    @PostMapping("publish")
    public void publishMessage(@RequestBody @Valid MqttPublishModel messagePublishModel,
                               BindingResult bindingResult) throws org.eclipse.paho.client.mqttv3.MqttException {
        if (bindingResult.hasErrors()) {
            throw new MqttException(500);
        }

        MqttMessage mqttMessage = new MqttMessage(messagePublishModel.getMessage().getBytes());
        mqttMessage.setQos(messagePublishModel.getQos());
        mqttMessage.setRetained(messagePublishModel.getRetained());

        mqtt.getClient().publish(messagePublishModel.getTopic(), mqttMessage);
    }

    @GetMapping("subscribe")
    public List<MqttSubscribeModel> subscribeChannel(@RequestParam(value = "topic") String topic,
                                                     @RequestParam(value = "wait_millis") Integer waitMillis)
            throws org.eclipse.paho.client.mqttv3.MqttException {
        List<MqttSubscribeModel> messages = new ArrayList<>();
        CountDownLatch countDownLatch = new CountDownLatch(10);
        mqtt.getClient().subscribeWithResponse(topic, (s, mqttMessage) -> {
            MqttSubscribeModel mqttSubscribeModel = new MqttSubscribeModel();
            mqttSubscribeModel.setId(mqttMessage.getId());
            mqttSubscribeModel.setMessage(new String(mqttMessage.getPayload()));
            mqttSubscribeModel.setQos(mqttMessage.getQos());
            messages.add(mqttSubscribeModel);
            countDownLatch.countDown();
        });


        return messages;
    }


}
