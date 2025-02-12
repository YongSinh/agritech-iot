package com.agritechiot.agritech_iot.controller;

import com.agritechiot.agritech_iot.config.Mqtt;
import com.agritechiot.agritech_iot.dto.ApiResponse;
import com.agritechiot.agritech_iot.dto.request.MqttPublishReq;
import com.agritechiot.agritech_iot.model.MqttPublishModel;
import com.agritechiot.agritech_iot.model.MqttSubscribeModel;
import com.agritechiot.agritech_iot.service.mqtt.Publisher;
import com.agritechiot.agritech_iot.service.mqtt.SubscriberImp;
import com.agritechiot.agritech_iot.util.JsonUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SampleController {
    private final SubscriberImp subscriberImp;
    private final SimpMessagingTemplate messagingTemplate;
    private final Publisher publisher;
    @PostMapping("/sample")
    public ResponseEntity<?> sample(@RequestBody MqttPublishReq req) throws Exception {
        publisher.publish(req.getTopic(), JsonUtil.objectToJsonString(req.getMessage()), req.getQos(), req.getRetained());
        return ResponseEntity.ok(new ApiResponse<>(req));
    }

    @PostMapping("/send/genMessage")
    public void sendGenMessage(@RequestBody Object message) {
        messagingTemplate.convertAndSend("/topic/public", message);
    }

    @PostMapping("publish")
    public void publishMessage(@RequestBody @Valid MqttPublishModel messagePublishModel,
                               BindingResult bindingResult) throws org.eclipse.paho.client.mqttv3.MqttException {
        if (bindingResult.hasErrors()) {
            throw new MqttException(Integer.parseInt("SOME_PARAMETERS_INVALID"));
        }

        MqttMessage mqttMessage = new MqttMessage(messagePublishModel.getMessage().getBytes());
        mqttMessage.setQos(messagePublishModel.getQos());
        mqttMessage.setRetained(messagePublishModel.getRetained());

        Mqtt.getInstance().publish(messagePublishModel.getTopic(), mqttMessage);
    }

    @GetMapping("subscribe")
    public List<MqttSubscribeModel> subscribeChannel(@RequestParam(value = "topic") String topic,
                                                     @RequestParam(value = "wait_millis") Integer waitMillis)
            throws InterruptedException, org.eclipse.paho.client.mqttv3.MqttException {
        List<MqttSubscribeModel> messages = new ArrayList<>();
        CountDownLatch countDownLatch = new CountDownLatch(10);
        Mqtt.getInstance().subscribeWithResponse(topic, (s, mqttMessage) -> {
            MqttSubscribeModel mqttSubscribeModel = new MqttSubscribeModel();
            mqttSubscribeModel.setId(mqttMessage.getId());
            mqttSubscribeModel.setMessage(new String(mqttMessage.getPayload()));
            mqttSubscribeModel.setQos(mqttMessage.getQos());
            messages.add(mqttSubscribeModel);
            countDownLatch.countDown();
        });

        countDownLatch.await(waitMillis, TimeUnit.MILLISECONDS);

        return messages;
    }


}
