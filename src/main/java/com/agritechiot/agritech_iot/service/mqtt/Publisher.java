package com.agritechiot.agritech_iot.service.mqtt;

public interface Publisher {
    void temperature();

    void humidity();

    void waterFlow();

    void soilMoisture();
}
