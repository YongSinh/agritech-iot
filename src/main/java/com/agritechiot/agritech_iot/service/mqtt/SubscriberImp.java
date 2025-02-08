package com.agritechiot.agritech_iot.service.mqtt;

import com.agritechiot.agritech_iot.config.Mqtt;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SubscriberImp implements Subscriber {
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

    @Override
    public void humidity() throws MqttException {
        Mqtt.getInstance().subscribe("humidity", (topic, message) -> {
            String payload = new String(message.getPayload());
            log.info("📥 Received message on topic {}: {}", topic, payload);
            processMessage(payload);
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
