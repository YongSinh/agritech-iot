package com.agritechiot.agritech_iot.config;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Mqtt {
    private static final Logger LOGGER = LoggerFactory.getLogger(Mqtt.class);

    private static final String MQTT_PUBLISHER_ID = "spring-server-iot";
    private static final String MQTT_SERVER_ADDRESS = "tcp://127.0.0.1:1883";

    private static final String MQTT_USERNAME = "user_test"; // 🔹 Change this to your username
    private static final String MQTT_PASSWORD = "test@2025"; // 🔹 Change this to your password

    private static volatile IMqttClient instance;

    private Mqtt() {
        // Private constructor to prevent instantiation
    }

    public static IMqttClient getInstance() {
        if (instance == null) {
            synchronized (Mqtt.class) {
                if (instance == null) {
                    try {
                        instance = new MqttClient(MQTT_SERVER_ADDRESS, MQTT_PUBLISHER_ID);
                        connectClient();
                    } catch (MqttException e) {
                        LOGGER.error("Failed to create MQTT client", e);
                    }
                }
            }
        }
        return instance;
    }

    private static void connectClient() {
        try {
            if (instance != null && !instance.isConnected()) {
                MqttConnectOptions options = new MqttConnectOptions();
                options.setAutomaticReconnect(true);
                options.setCleanSession(true);
                options.setConnectionTimeout(10);
                options.setUserName(MQTT_USERNAME); // ✅ Set MQTT username
                options.setPassword(MQTT_PASSWORD.toCharArray()); // ✅ Set MQTT password

                instance.connect(options);
                LOGGER.info("Connected to MQTT broker at {}", MQTT_SERVER_ADDRESS);
            }
        } catch (MqttException e) {
            LOGGER.error("Failed to connect to MQTT broker", e);
        }
    }

    public static void disconnect() {
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
}