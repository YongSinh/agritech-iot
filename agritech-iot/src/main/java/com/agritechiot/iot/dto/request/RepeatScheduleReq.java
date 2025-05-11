package com.agritechiot.iot.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RepeatScheduleReq {
    private Integer id;
    private String day;
    private String time;
    private Boolean readSensor;
    private Boolean turnOnWater;
    private Integer duration;
    private String deviceId;
}
