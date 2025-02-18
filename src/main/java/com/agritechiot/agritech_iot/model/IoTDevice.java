package com.agritechiot.agritech_iot.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;
@Setter
@Getter
@Table("tbl_iotdevice")
@Builder
public class IoTDevice implements Persistable<String> {
    @Id
    @Column("deviceId")
    private String deviceId;
    private String name;
    private String controller;
    private String sensors;
    private String remark;

    @Transient
    @Builder.Default
    private boolean isNewEntry = true;
    @Override
    public String getId() {
        return deviceId; // Return the actual deviceId
    }

    @Override
    public boolean isNew() {
        return isNewEntry;
    }
}
