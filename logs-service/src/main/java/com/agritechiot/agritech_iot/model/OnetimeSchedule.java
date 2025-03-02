package com.agritechiot.agritech_iot.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.time.LocalTime;

@Document(collection = "tbl_onetime_schedule")
@Getter
@Setter
public class OnetimeSchedule {
    @Id
    private String id;
    private LocalDate date;
    private LocalTime time;
    @Field("read_sensor")
    private Boolean readSensor;
    @Field("turnOn_water")
    private Boolean turnOnWater;
    private Integer duration;
    @Field("deviceid")
    private String deviceId;
}
