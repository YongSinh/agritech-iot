package com.agritechiot.agritech_iot.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "tbl_interval_schedule")
@Getter
@Setter
public class IntervalSchedule {
    @Id
    private String id;
    private Integer interval;
    @Field("read_sensor")
    private Boolean readSensor;
    @Field("turnOn_water")
    private Boolean turnOnWater;
    private Integer duration;
    @Field("deviceid")
    private String deviceId;
    @Field("run_datetime")
    private Integer runDatetime;
}
