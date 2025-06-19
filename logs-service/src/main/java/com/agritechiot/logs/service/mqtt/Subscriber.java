package com.agritechiot.logs.service.mqtt;

import org.eclipse.paho.client.mqttv3.MqttException;

public interface Subscriber {
    void sub() throws MqttException;

    void temperature() throws MqttException;
}
