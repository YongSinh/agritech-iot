package com.agritechiot.logs.service.mqtt;

import org.eclipse.paho.client.mqttv3.MqttException;

import javax.net.ssl.SSLException;

public interface Subscriber {
    void sub() throws MqttException, SSLException;

}
