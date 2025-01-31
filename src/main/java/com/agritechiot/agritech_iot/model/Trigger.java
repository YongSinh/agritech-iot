package com.agritechiot.agritech_iot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "tbl_trigger")
public class Trigger {
    @Id
    private String id;
    private LocalDateTime datetime;
    private String operator;
    private String sensor;
    private Integer value;
    private String action;
    private Integer duration;
    @JsonProperty("device_id")
    private String deviceId;
}
