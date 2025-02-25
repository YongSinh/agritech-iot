package com.agritechiot.agritech_iot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;


@Setter
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@Table("control_log")
public class ControlLog {
    @Id
    private String id;
    private LocalDateTime datetime;
    @JsonProperty("device_id")
    @Column("deviceId")
    private String deviceId;
    private String status;
    private Integer duration;
    private String sentBy;
}
