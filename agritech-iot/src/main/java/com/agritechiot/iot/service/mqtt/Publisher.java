package com.agritechiot.iot.service.mqtt;

import org.eclipse.paho.client.mqttv3.MqttException;
import reactor.core.publisher.Flux;

public interface Publisher {
    void publish(final String topic, final String payload, int qos, boolean retained) throws MqttException;
    Flux<String> getTopicMaster();
}
