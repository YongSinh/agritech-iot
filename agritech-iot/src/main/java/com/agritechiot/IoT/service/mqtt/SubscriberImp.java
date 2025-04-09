package com.agritechiot.IoT.service.mqtt;

import com.agritechiot.IoT.config.Mqtt;
import com.agritechiot.IoT.model.Trigger;
import com.agritechiot.IoT.service.TriggerService;
import com.agritechiot.IoT.util.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SubscriberImp implements Subscriber {

    private final TriggerService triggerService;

    @PostConstruct
    public void init() {
        try {
            sub();
            temperature();
            humidity();
            waterFlow();
            soilMoisture();
        } catch (MqttException e) {
            log.error("❌ Error subscribing to MQTT topic", e);
        }
    }


    private void processMessage(String message) {
        log.info("🔄 Processing message: {}", message);
    }

    @Override
    public void sub() throws MqttException {
        if (!Mqtt.getInstance().isConnected()) {
            log.warn("⚠️ MQTT Client is not connected! Trying to reconnect...");
            Mqtt.getInstance().connect();
        }

        log.info("📡 Subscribing to MQTT topic: test");

        Mqtt.getInstance().subscribe("test", (topic, message) -> {
            String payload = new String(message.getPayload());
            log.info("📥 Received message on topic {}: {}", topic, payload);
            processMessage(payload);
        });

        log.info("✅ Successfully subscribed to MQTT topic: test");
    }

    @Override
    public void temperature() throws MqttException {
        Mqtt.getInstance().subscribe("temperature", (topic, message) -> {
            String payload = new String(message.getPayload());
            log.info("📥 Received message on topic {}: {}", topic, payload);
            processMessage(payload);
        });
    }

    @Async
    @Override
    public void humidity() throws MqttException {
        Mqtt.getInstance().subscribe("humidity", (topic, message) -> {
            String res = new String(message.getPayload());
            JsonNode payload = JsonUtil.parseJson(res);
            log.info(String.valueOf(payload));
            log.info("date: {}", payload.path("datetime"));
            log.info("📥 Received message on topic {}: {}", topic, res);
            processMessage(res);
            Trigger trigger = new Trigger();
            trigger.setSensor(payload.path("datetime").asText());
            trigger.setDeviceId(payload.path("device_id").asText());
            trigger.setOperator(payload.path("operator").asText());
            trigger.setValue(payload.path("value").intValue());
            trigger.setDuration(payload.path("duration").intValue());
            trigger.setAction(payload.path("action").asText());
            triggerService.saveTrigger(trigger)
                    .doOnSuccess(savedTrigger -> log.info("✅ Trigger saved successfully: {}", savedTrigger))
                    .doOnError(error -> log.error("❌ Failed to save trigger", error))
                    .subscribe();  // Subscribe to execute the operation
        });
    }

    @Override
    public void waterFlow() throws MqttException {
        Mqtt.getInstance().subscribe("waterFlow", (topic, message) -> {
            String payload = new String(message.getPayload());
            log.info("📥 Received message on topic {}: {}", topic, payload);
            processMessage(payload);
        });
    }

    @Override
    public void soilMoisture() throws MqttException {
        Mqtt.getInstance().subscribe("soilMoisture", (topic, message) -> {
            String payload = new String(message.getPayload());
            log.info("📥 Received message on topic {}: {}", topic, payload);
            processMessage(payload);
        });
    }

}
