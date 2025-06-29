package com.agritechiot.iot.dto.request;

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
    private String deviceId;
    private Integer runDatetime;
}
