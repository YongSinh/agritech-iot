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
    private String run;
    private String type;
}
