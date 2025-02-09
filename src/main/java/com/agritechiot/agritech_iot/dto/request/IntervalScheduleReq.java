package com.agritechiot.agritech_iot.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

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
