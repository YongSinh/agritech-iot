package com.agritechiot.agritech_iot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IoTDeviceDto {
    private String deviceId;
    private String name;
    private String controller;
    private String sensors;
    private String remark;
}