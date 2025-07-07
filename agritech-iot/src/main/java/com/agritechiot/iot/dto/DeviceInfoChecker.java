package com.agritechiot.iot.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DeviceInfoChecker {
    private String deviceId;
    private String status;
    private String run;
    private String sleep;
}
