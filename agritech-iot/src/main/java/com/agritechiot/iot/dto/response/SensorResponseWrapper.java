package com.agritechiot.iot.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SensorResponseWrapper {
    private String device;
    private String id;
    private String status;
    private List<SensorTransformer.SensorResponse> data;

    public SensorResponseWrapper(SensorTransformer sensor) {
        this.device = sensor.getDevice();
        this.id = sensor.getId();
        this.status = sensor.getStatus();
        this.data = sensor.toResponseList();
    }
}
