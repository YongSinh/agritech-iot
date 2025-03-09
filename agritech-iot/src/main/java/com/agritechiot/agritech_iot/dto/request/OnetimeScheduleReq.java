package com.agritechiot.agritech_iot.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@NoArgsConstructor
public class OnetimeScheduleReq {
    private Integer id;
    @JsonProperty("date")
    private String date;
    private String time;
    private Boolean readSensor;
    private Boolean turnOnWater;
    private Integer duration;
    @JsonProperty("device_id")
    private String deviceId;
}
