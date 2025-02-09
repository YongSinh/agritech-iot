package com.agritechiot.agritech_iot.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Setter
@Getter
@AllArgsConstructor
public class IntervalScheduleReq {
    private Integer id;
    private Integer interval;
    private Boolean read_sensor;
    private Boolean turnOn_water;
    private Integer duration;
//    @JsonProperty("device_id")
    private String deviceId;
    private Integer run_datetime;
}
