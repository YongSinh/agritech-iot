package com.agritechiot.agritech_iot.service.mqtt;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

public interface Publisher {
    void publish(final String topic, final String payload, int qos, boolean retained) throws MqttException;

}
