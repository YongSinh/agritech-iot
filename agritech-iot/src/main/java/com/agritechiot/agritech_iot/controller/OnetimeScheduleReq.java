package com.agritechiot.agritech_iot.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
@Getter
@Setter
@NoArgsConstructor
public class OnetimeScheduleReq {
    private Integer id;
    private LocalDate date;
    private LocalTime time;
    private Boolean readSensor;
    private Boolean turnOnWater;
    private Integer duration;
    @JsonProperty("device_id")
    private String deviceId;
}
