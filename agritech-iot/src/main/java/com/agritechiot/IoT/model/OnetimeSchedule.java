package com.agritechiot.IoT.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalTime;

@Setter
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "tbl_onetime_schedule")
public class OnetimeSchedule {
    @Id
    private Integer id;
    private String date;
    private LocalTime time;
    @Column("read_sensor")
    private Boolean readSensor;
    @Column("turnOn_water")
    private Boolean turnOnWater;
    private Integer duration;
    @JsonProperty("device_id")
    @Column("deviceId")
    private String deviceId;
}
