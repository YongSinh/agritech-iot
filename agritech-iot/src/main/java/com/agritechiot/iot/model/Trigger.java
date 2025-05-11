package com.agritechiot.iot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Setter
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "tbl_trigger")
public class Trigger {
    @Id
    private Integer id;
    private String operator;
    private String sensor;
    private Integer value;
    private String action;
    private Integer duration;
    @Column("deviceId")
    private String deviceId;
}
