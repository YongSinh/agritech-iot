package com.agritechiot.iot.service.mqtt;

import com.agritechiot.iot.config.Mqtt;
import com.agritechiot.iot.model.Trigger;
import com.agritechiot.iot.repository.IoTDeviceRepo;
import com.agritechiot.iot.service.TriggerService;
import com.agritechiot.iot.util.JsonUtil;
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
    private final IoTDeviceRepo ioTDeviceRepo;
    private final Mqtt mqtt;

    @PostConstruct
    public void init() {
        try {
            sub();
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
        if (!mqtt.getClient().isConnected()) {
            log.warn("⚠️ MQTT Client is not connected! Trying to reconnect...");
            mqtt.getClient().connect();
        }

        log.info("📡 Subscribing to MQTT topic: test");

        mqtt.getClient().subscribe("test", (topic, message) -> {
            String payload = new String(message.getPayload());
            log.info("📥 Received message on topic {}: {}", topic, payload);
            processMessage(payload);
        });

        log.info("✅ Successfully subscribed to MQTT topic: test");
    }

    @Override
    public void temperature() throws MqttException {
        mqtt.getClient().subscribe("temperature", (topic, message) -> {
            String payload = new String(message.getPayload());
            log.info("📥 Received message on topic {}: {}", topic, payload);
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
        mqtt.getClient().subscribe("sensors/mqtt_out", (topic, message) -> {
            String payload = new String(message.getPayload());
            log.info("📥 Received message on topic {}: {}", topic, payload);
            processMessage(payload);
        });
    }

    @Override
    public void soilMoisture() throws MqttException {
        mqtt.getClient().subscribe("sensors/mqtt_in/MasterLoRa_1", (topic, message) -> {
            String payload = new String(message.getPayload());
            log.info("📥 Received message on topic {}: {}", topic, payload);
            processMessage(payload);
        });
    }

    @Override
    public void test() throws MqttException {
        ioTDeviceRepo.findAllTopicNames() // Returns Flux<String>
                .doOnNext(topic -> {
                    try {
                        mqtt.getClient().subscribe(topic, (t, message) -> {
                            String payload = new String(message.getPayload());
                            log.info("📥 Received message on topic {}: {}", t, payload);
                            processMessage(payload);
                        });
                        log.info("✅ Subscribed to topic: {}", topic);
                    } catch (MqttException e) {
                        log.error("❌ Failed to subscribe to topic: {}", topic, e);
                    }
                })
                .subscribe(); // Important: subscribe to trigger execution
    }

}
