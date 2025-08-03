package com.agritechiot.iot.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CheckDeviceStatusReq {
    private String device;
    private String id;
    private String status;
}
