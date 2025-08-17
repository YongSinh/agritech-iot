package com.agritechiot.iot.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class DeviceCommandReq {
    private Integer controlLogId;
    private String deviceId;
    private String type;
    private String state;
    private String status;
    private String duration;
    private Boolean valveDuration;
    private String value;
    private String sensor;
}
