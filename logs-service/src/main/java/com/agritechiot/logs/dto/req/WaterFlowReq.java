package com.agritechiot.logs.dto.req;

import com.agritechiot.logs.model.SensorLog;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class WaterFlowReq {
    private String deviceId;
    private Double flowRate;
    private Double flowQuantity;
    private Double totalWater;
    private String action;

    public SensorLog toSensorLog() {
        return SensorLog.builder()
                .deviceId(deviceId)
                .status("water_flow")
                .dateTime(LocalDateTime.now())
                .action(action)
                .flowRate(flowRate)
                .totalWater(totalWater)
                .measurements(Map.of(
                        "flow_rate", flowRate,
                        "flow_quantity", flowQuantity,
                        "total_water", totalWater
                ))
                .build();
    }
}
