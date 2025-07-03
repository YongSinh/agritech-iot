package com.agritechiot.logs.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MqttMessageRes {
    private String deviceId;
    private String value;
    private String status;
    private Double valveStatus; // Only for soil_moisture
    private Double flowRate; // Only for water_flow
    private Double totalWater; // Only for water_flow


    public String determineSensorType() {
        if (valveStatus != null) {
            return status;
        } else if (flowRate != null || totalWater != null) {
            return "water_flow";
        }
        return "unknown";
    }
}
