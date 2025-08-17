package com.agritechiot.iot.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class DeviceCommandReq2 {
    private String id;
    private String device;
    private String set;
    private String state;
    private String status;
    private String value;
    private String duration;
}
