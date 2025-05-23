package com.agritechiot.logs.service.mqtt;

import org.eclipse.paho.client.mqttv3.MqttException;

public interface Publisher {
    void publish(final String topic, final String payload, int qos, boolean retained) throws MqttException;

}
