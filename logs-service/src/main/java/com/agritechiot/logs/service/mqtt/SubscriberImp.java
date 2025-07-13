package com.agritechiot.logs.service.mqtt;

import com.agritechiot.logs.config.Mqtt;
import com.agritechiot.logs.service.SensorLogService;
import com.agritechiot.logs.util.JsonUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SubscriberImp implements Subscriber {
    private final Mqtt mqtt;
    private final SensorLogService service;

    @PostConstruct
    public void init() {
        try {
            sub();
            saveSensorLog();
        } catch (MqttException e) {
            log.error("❌ Error subscribing to MQTT topic", e);
        }
    }

    private void logMessage(String message, String topic) {
        log.info("RES_MQTT {} - {}", message, topic);
    }

    private void processMessage(String message) {
        log.info("🔄 Processing message: {}", message);
    }

    @Override
    public void sub() throws MqttException {
        if (!mqtt.getClient().isConnected()) {
            log.warn("⚠️ MQTT Client is not connected! Trying to reconnect...");
            mqtt.getClient().connect();
        }

        log.info("📡 Subscribing to MQTT topic: test");

        mqtt.getClient().subscribe("#", (topic, message) -> {
            String payload = new String(message.getPayload());
            logMessage(payload, topic);
            processMessage(payload);
        });

        log.info("✅ Successfully subscribed to MQTT topic: test");
    }

    private void saveSensorLog() throws MqttException {
        mqtt.getClient().subscribe("#", (topic, message) -> {
            String res = new String(message.getPayload());
            Object payload = JsonUtil.fromJson(res, Object.class);
            log.info("Object: {}", JsonUtil.toJson(payload));
            log.info("📥 Received message on topic {}: {}", topic, res);
            processMessage(res);
            service.saveSensorLog(payload)
                    .doOnSuccess(savedTrigger -> log.info("✅ Trigger saved successfully: {}", savedTrigger))
                    .doOnError(error -> log.error("❌ Failed to save trigger", error))
                    .subscribe();
        });
    }

}
