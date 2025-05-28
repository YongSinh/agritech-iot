package com.agritechiot.iot.config;

import jakarta.annotation.PostConstruct;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Mqtt {

    private static final Logger LOGGER = LoggerFactory.getLogger(Mqtt.class);
    private static final String MQTT_PUBLISHER_ID = "spring-server-iot";
    @Value("${mqtt.username}")
    private String mqttUsername;
    @Value("${mqtt.password}")
    private String mqttPassword;
    @Value("${mqtt.url}")
    private String mqttUrl;
    private IMqttClient instance;

    @PostConstruct
    public void init() {
        try {
            instance = new MqttClient(mqttUrl, "spring-server-iot");
            connectClient();
        } catch (MqttException e) {
            LOGGER.error("Failed to create MQTT client", e);
        }
    }

    private void connectClient() {
        try {
            if (instance != null && !instance.isConnected()) {
                MqttConnectOptions options = new MqttConnectOptions();
                options.setAutomaticReconnect(true);
                options.setCleanSession(true);
                options.setConnectionTimeout(10);
                options.setUserName(mqttUsername);
                options.setPassword(mqttPassword.toCharArray());

                instance.connect(options);
                LOGGER.info("Connected to MQTT broker at {}", mqttUrl);
            }
        } catch (MqttException e) {
            LOGGER.error("Failed to connect to MQTT broker", e);
        }
    }

    public void disconnect() {
        try {
            if (instance != null && instance.isConnected()) {
                instance.disconnect();
                instance.close();
                LOGGER.info("Disconnected from MQTT broker");
            }
        } catch (MqttException e) {
            LOGGER.error("Error disconnecting from MQTT broker", e);
        }
    }

    public IMqttClient getClient() {
        return instance;
    }
}