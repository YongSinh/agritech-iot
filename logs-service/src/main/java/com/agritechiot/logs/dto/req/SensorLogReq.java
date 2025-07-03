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
public class SensorLogReq {
    private String deviceId;
    private Double valve;
    private String action;
    private String status;

    public SensorLog toSensorLog() {
        return SensorLog.builder()
                .deviceId(deviceId)
                .status(status)
                .dateTime(LocalDateTime.now())
                .action(action)
                .valveStatus(valve)
                .measurements(Map.of("valve", valve))
                .build();
    }
}
