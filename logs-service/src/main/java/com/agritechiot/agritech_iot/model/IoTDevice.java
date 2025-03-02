package com.agritechiot.agritech_iot.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "tbl_iotdevice")
@Getter
@Setter
public class IoTDevice {
    @Id
    private String id;
    @Field("deviceid")
    private String deviceId;
    private String name;
    private String sensors;
    private String remark;
}
