package com.agritechiot.agritech_iot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Setter
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@Table("tbl_iotdevice")
public class IoTDevice {
    @Id
    @JsonProperty("device_id")
    private Integer deviceid;
    private String name;
    private String controller;
    private String sensors;
    private String remark;
}
