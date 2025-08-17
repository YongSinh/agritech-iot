package com.agritechiot.logs.service.mqtt;

import com.agritechiot.logs.config.Mqtt;
import com.agritechiot.logs.service.SensorLogService;
import com.agritechiot.logs.service.integration.IotService;
import com.agritechiot.logs.util.JsonUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.net.ssl.SSLException;

@Service
@Slf4j
@RequiredArgsConstructor
public class SubscriberImp implements Subscriber {
    private final Mqtt mqtt;
    private final SensorLogService service;
    private final IotService iotService;

//    @PostConstruct
//    public void init() {
//        try {
//            sub();
//        } catch (MqttException e) {
//            log.error("❌ Error subscribing to MQTT topic", e);
//        } catch (SSLException e) {
//            throw new RuntimeException(e);
//        }
//    }

    private void logMessage(String message, String topic) {
        log.info("RES_MQTT {} - {}", message, topic);
    }

    private void processMessage(String message) {
        log.info("🔄 Processing message: {}", message);
    }

    @Override
    public void sub() throws MqttException, SSLException {
        if (!mqtt.getClient().isConnected()) {
            log.warn("⚠️ MQTT Client is not connected! Trying to reconnect...");
            mqtt.getClient().connect();
            log.info("🔄 MQTT Client reconnected successfully.");
        }

        iotService.getTopic()
                .flatMapMany(Flux::fromIterable) // convert List<TopicRes> -> Flux<TopicRes>
                .doOnSubscribe(sub -> log.info("📡 Fetching topics from IoT service..."))
                .flatMap(res -> Mono.fromRunnable(() -> {
                    try {
                        String topicOut = res.getTopicOut();
                        String topicIn = res.getTopic();
                        log.info("📌 Subscribing to IN topic: {}", topicIn);
                        log.info("📌 Subscribing to OUT topic: {}", topicOut);

                        saveSensorLog(topicIn);
                        saveSensorLog(topicOut);

                        log.info("✅ Successfully subscribed to IN/OUT topics [{} , {}]", topicIn, topicOut);
                    } catch (Exception e) {
                        log.error("❌ Failed to subscribe to topic: {}", res.getTopic(), e);
                    }
                }))
                .doOnComplete(() -> log.info("🎉 All topics subscribed successfully."))
                .doOnError(e -> log.error("🔥 Error while subscribing to topics", e))
                .subscribe();
    }


    private void saveSensorLog(String mqttTopic) throws MqttException {
        mqtt.getClient().subscribe(mqttTopic, (topic, message) -> {
            try {
                String res = new String(message.getPayload());
                Object payload = JsonUtil.fromJson(res, Object.class);

                log.info("📥 Received message on [{}]: {}", topic, res);
                logMessage(res, topic);
                processMessage(res);

                service.saveSensorLog(payload, mqttTopic)
                        .doOnSuccess(saved -> log.info("✅ Trigger saved successfully for [{}]: {}", topic, saved))
                        .doOnError(error -> log.error("❌ Failed to save trigger for [{}]", topic, error))
                        .subscribe();

            } catch (Exception e) {
                log.error("⚠️ Error while processing message from [{}]", mqttTopic, e);
            }
        });
    }

}
