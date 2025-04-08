package com.agritechiot.IoT.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("device_id")
    private String deviceId;
}
