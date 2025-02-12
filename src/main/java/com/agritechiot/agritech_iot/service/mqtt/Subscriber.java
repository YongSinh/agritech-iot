package com.agritechiot.agritech_iot.service.mqtt;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

public interface Subscriber {
    void sub() throws MqttException;

    void temperature() throws MqttException;

    void humidity() throws MqttException;

    void waterFlow() throws MqttException;

    void soilMoisture() throws MqttException;
}
