package com.agritechiot.agritech_iot.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "sensor_log")
@Getter
@Setter
public class SensorLog {
    @Id
    private String id;
    private LocalDateTime dateTime;
    @Field("deviceid")
    private String deviceId;
    private Double temperature;
    private Double humidity;
    @Field("sensorid")
    private Double sensorId;
    @Field("soil_moisture")
    private Double soilMoisture;
    @Field("flowRate")
    private Double flow_rate;
    @Field("flow_quantity")
    private Double flowQuantity;
    @Field("total_water")
    private Double totalWater;
}
