package com.agritechiot.iot.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class ControlLogReq {
    private Integer id;
    private LocalDateTime dateTime;
    private String deviceId;
    private Boolean status;
    private Integer duration;
    private String sentBy;
    private LocalDate startDate;
    private LocalDate endDate;
}
