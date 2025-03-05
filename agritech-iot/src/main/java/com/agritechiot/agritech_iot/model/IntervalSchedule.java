package com.agritechiot.agritech_iot.model;

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
@Table(name = "tbl_interval_schedule")
public class IntervalSchedule {
    @Id
    private Integer id;
    private Integer interval;
    private Integer duration;
    @Column("run_datetime")
    private Integer runDatetime;
    @Column("turnOn_water")
    private Boolean turnOnWater;
    @JsonProperty("device_id")
    @Column("deviceId")
    private String deviceId;
}
