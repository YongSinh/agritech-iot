package com.agritechiot.IoT.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class IntervalScheduleReq {
    private Integer id;
    private Integer interval;
    private Boolean readSensor;
    private Boolean turnOnWater;
    private Integer duration;
    //    @JsonProperty("device_id")
    private String deviceId;
    private Integer runDatetime;
}
