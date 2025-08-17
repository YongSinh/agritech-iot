package com.agritechiot.logs.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FilterReq {
    private String topic;
    private String status;
    private String deviceId;
    private Integer limit = 10;
}
