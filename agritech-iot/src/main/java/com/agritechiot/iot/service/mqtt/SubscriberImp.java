package com.agritechiot.iot.service.mqtt;

import com.agritechiot.iot.config.Mqtt;
import com.agritechiot.iot.constant.GenConstant;
import com.agritechiot.iot.dto.response.MqttMessageRes;
import com.agritechiot.iot.repository.MqttTopicRepo;
import com.agritechiot.iot.service.IoTDeviceService;
import com.agritechiot.iot.service.LogService;
import com.agritechiot.iot.util.GenUtil;
import com.agritechiot.iot.util.JsonUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class SubscriberImp implements Subscriber {
    private final LogService logService;
    private final SimpMessagingTemplate messagingTemplate;
    private final IoTDeviceService ioTDeviceService;
    private final MqttTopicRepo mqttTopicRepo;
    private final Mqtt mqtt;
    @Value("${master.topic}")
    private String[] topics;

    @PostConstruct
    public void init() {
        try {
            sub();
            updateStateDevice();
        } catch (MqttException e) {
            log.error("❌ Error subscribing to MQTT topic", e);
        }
    }


    private void processMessage(String message) {
        log.info("🔄 Processing message: {}", message);
    }

    private void logMessage(String message, String topic) {
        logService.logMqtt("RES_MQTT", message, topic);
    }

    @Override
    public void sub() throws MqttException {
        if (!mqtt.getClient().isConnected()) {
            log.warn("⚠️ MQTT Client is not connected! Trying to reconnect...");
            mqtt.getClient().connect();
        }
        log.info("📡 Subscribing to MQTT topic: test");

        mqtt.getClient().subscribe("test", (topic, message) -> {
            String payload = new String(message.getPayload());
            logMessage(payload, topic);
            processMessage(payload);
        });

        log.info("✅ Successfully subscribed to MQTT topic: test");
    }


    @Override
    public void updateStateDevice() throws MqttException {
        mqttTopicRepo.findByIsNotDeleted()
                .flatMap(topic -> {
                    try {
                        mqtt.getClient().subscribe(topic.getTopic(), (t, message) -> {
                            String res = new String(message.getPayload());
                            MqttMessageRes dto = JsonUtil.fromJson(res, MqttMessageRes.class);
                            log.info("Res: {}", dto);
                            GenUtil.validateFields(dto);
                            logMessage(res, topic.toString());
                            ioTDeviceService.updateDeviceStats(dto.getDeviceId(), GenUtil.checkOffAndOn(dto.getValue()))
                                    .doOnSuccess(saveDevice -> log.info("✅ saved successfully: {}", saveDevice))
                                    .doOnError(error -> log.error("❌ Failed to save", error))
                                    .subscribe();
                            processMessage(res);
                        });
                        log.info(GenConstant.SUBSCRIBE_MSG_LOG, topic);
                    } catch (MqttException e) {
                        log.error("❌ Failed to subscribe to topic: {}", topic, e);
                    }
                    return Mono.just(topics);  // Continue with processing
                }).subscribe();

    }

}
