package com.agritechiot.IoT;

import com.agritechiot.IoT.config.Mqtt;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AgritechIotApplicationTests {

    @Test
    void contextLoads() {
        IMqttClient mqttClient = Mqtt.getInstance();
        if (mqttClient.isConnected()) {
            System.out.println("Connected to MQTT!");
        }
        // Disconnect when shutting down
        Runtime.getRuntime().addShutdownHook(new Thread(Mqtt::disconnect));

    }

}
