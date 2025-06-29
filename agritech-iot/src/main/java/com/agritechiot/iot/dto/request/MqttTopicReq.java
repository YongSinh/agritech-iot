package com.agritechiot.iot.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class MqttTopicReq {
    private Integer id;
    private String topic;
    private String createdBy;
    private Boolean isRemoved;
    private LocalDateTime deletedAt;
}
