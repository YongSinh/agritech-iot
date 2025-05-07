package com.agritechiot.iot.service.mqtt;

import com.agritechiot.iot.config.Mqtt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PublisherImp implements Publisher {
    @Override
    public void publish(String topic, String payload, int qos, boolean retained) throws MqttException {
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setPayload(payload.getBytes());
        mqttMessage.setQos(qos);
        mqttMessage.setRetained(retained);
        Mqtt.getInstance().publish(topic, mqttMessage);
        //mqttClient.publish(topic, payload.getBytes(), qos, retained);
        // Mqtt.getInstance().disconnect();
    }

}
