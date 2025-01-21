package com.agritechiot.agritech_iot.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "tbl_trigger")
@Getter
@Setter
public class Trigger {
    @Id
    private String id;
    private LocalDateTime dateTime;
    private String operator;
    private String sensor;
    private Integer value;
    private String action;
    private Integer duration;
    @Field("deviceid")
    private String deviceId;
}
