package com.agritechiot.iot.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class TriggerReq {
    private String operator;
    private String sensor;
    private Integer value;
    private String action;
    private Integer duration;
    private List<String> deviceId;
    private Boolean isRemoved;
    private LocalDateTime deletedAt;
    private Integer sleepDuration;
}
