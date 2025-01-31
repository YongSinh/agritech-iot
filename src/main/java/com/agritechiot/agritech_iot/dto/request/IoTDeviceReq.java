package com.agritechiot.agritech_iot.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class IoTDeviceReq {
    @JsonProperty("device_id")
    private Integer deviceId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("sensors")
    private String sensors;
    @JsonProperty("remark")
    private String remark;
}
