package com.agritechiot.iot.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class IoTDeviceReq {
    private String deviceId;
    private String name;
    private String sensors;
    private String remark;
    private String controller;
}
