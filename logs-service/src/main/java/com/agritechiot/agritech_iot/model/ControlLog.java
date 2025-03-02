package com.agritechiot.agritech_iot.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "control_log")
@Getter
@Setter
public class ControlLog {
    @Id
    private String id;
    private LocalDateTime dateTime;
    @Field("deviceid")
    private String deviceId;
    private String status;
    private Integer duration;
    @Field("sentby")
    private String sentBy;
}
