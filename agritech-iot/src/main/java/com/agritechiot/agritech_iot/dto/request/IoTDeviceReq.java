package com.agritechiot.agritech_iot.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class IoTDeviceReq {
    //    @JsonProperty("device_id")
    private String deviceId;
    private String name;
    private String sensors;
    private String remark;
    private String controller;
}
