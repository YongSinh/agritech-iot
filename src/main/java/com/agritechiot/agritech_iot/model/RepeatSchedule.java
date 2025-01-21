package com.agritechiot.agritech_iot.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalTime;

@Document(collection = "tbl_repeat_schedule")
@Getter
@Setter
public class RepeatSchedule {
    @Id
    private String id;
    private String day;
    private LocalTime time;
    @Field("read_sensor")
    private Boolean readSensor;
    @Field("turnOn_water")
    private Boolean turnOnWater;
    private Integer duration;
    @Field("deviceid")
    private String deviceId;
}
