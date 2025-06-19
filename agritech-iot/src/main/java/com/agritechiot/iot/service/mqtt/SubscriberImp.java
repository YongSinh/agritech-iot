package com.agritechiot.iot.service.mqtt;

import com.agritechiot.iot.config.Mqtt;
import com.agritechiot.iot.model.Trigger;
import com.agritechiot.iot.service.LogService;
import com.agritechiot.iot.service.TriggerService;
import com.agritechiot.iot.util.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@Slf4j
@RequiredArgsConstructor
public class SubscriberImp implements Subscriber {
    private final LogService logService;
    private final SimpMessagingTemplate messagingTemplate;

    @Value("${master.topic}")
    private String[] topics;
    private final Mqtt mqtt;

    @PostConstruct
    public void init() {
        try {
            sub();
            waterFlow();
            soilMoisture();
            test();
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
    public void temperature() throws MqttException {
        mqtt.getClient().subscribe("temperature", (topic, message) -> {
            String payload = new String(message.getPayload());
            logMessage(payload, topic);
            processMessage(payload);
        });
    }

    @Async
    @Override
    public void humidity() throws MqttException {
        mqtt.getClient().subscribe("humidity", (topic, message) -> {
            String res = new String(message.getPayload());
            JsonNode payload = JsonUtil.parseJson(res);
            log.info(String.valueOf(payload));
            log.info("date: {}", payload.path("datetime"));
            processMessage(res);
        });
    }

    @Override
    public void waterFlow() throws MqttException {
        mqtt.getClient().subscribe("sensors/mqtt_out", (topic, message) -> {
            String payload = new String(message.getPayload());
            logMessage(payload, topic);
            processMessage(payload);
        });
    }

    @Override
    public void soilMoisture() throws MqttException {
        mqtt.getClient().subscribe("sensors/mqtt_in/MasterLoRa_1", (topic, message) -> {
            String payload = new String(message.getPayload());
            logMessage(payload, topic);
            processMessage(payload);
        });
    }

    @Override
    public void test() {
        Arrays.stream(topics)
                .forEach(topic -> {
                    // Process each topic
                    try {
                        mqtt.getClient().subscribe(topic, (t, message) -> {
                            String payload = new String(message.getPayload());
                            logMessage( payload, topic);
                            messagingTemplate.convertAndSend("/topic/genMessage", payload);
                            processMessage(payload);
                        });
                        log.info("✅ Subscribed to topic: {}", topic);
                    } catch (MqttException e) {
                        log.error("❌ Failed to subscribe to topic: {}", topic, e);
                    }
                });
    }

}
