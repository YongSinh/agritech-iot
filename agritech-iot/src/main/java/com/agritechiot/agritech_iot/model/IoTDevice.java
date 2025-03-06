package com.agritechiot.agritech_iot.model;

import com.agritechiot.agritech_iot.dto.response.IoTDeviceDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("tbl_iotdevice")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IoTDevice implements Persistable<String> {
    @Id
    @Column("deviceId")
    private String deviceId;
    private String name;
    private String controller;
    private String sensors;
    private String remark;

    @Transient
//    @Builder.Default
    private boolean isNewEntry = true;

    @Override
    public String getId() {
        return deviceId; // Return the actual deviceId
    }

    @Override
    public boolean isNew() {
        return isNewEntry;
    }

    public IoTDeviceDto toDto() {
        return new IoTDeviceDto(deviceId, name, controller, sensors, remark);
    }
}
