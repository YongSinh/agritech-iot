package com.agritechiot.iot.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SensorTransformer {
    private String device;
    private String id;
    private String status;
    private String valve;

    @JsonProperty("water_flow_rate")
    private String waterFlowRate;

    @JsonProperty("water_flow_quantity")
    private String waterFlowQuantity;

    @JsonProperty("water_total")
    private String waterTotal;

    @JsonProperty("soil_moisture")
    private String soilMoisture;

    @JsonProperty("soil_temperature")
    private String soilTemperature;

    @JsonProperty("ambien_temperature")
    private String ambienTemperature;

    private String voltage;

    // ✅ Mapper method to convert into response list
    public List<SensorResponse> toResponseList() {
        List<SensorResponse> list = new ArrayList<>();

        if (soilTemperature != null) {
            list.add(new SensorResponse("Soil Temp", Double.valueOf(soilTemperature), "soil_temperature"));
        }
        if (ambienTemperature != null) {
            list.add(new SensorResponse("Ambient Temp", Double.valueOf(ambienTemperature), "ambien_temperature"));
        }
        if (voltage != null) {
            list.add(new SensorResponse("Voltage", Double.valueOf(voltage), "voltage"));
        }

        return list;
    }

    @Getter
    @Setter
    public static class SensorResponse {
        private String label;
        private Double value;
        private String id;

        public SensorResponse(String label, Double value, String id) {
            this.label = label;
            this.value = value;
            this.id = id;
        }
    }
}
