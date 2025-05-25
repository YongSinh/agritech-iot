package com.agritechiot.iot.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalTime;

@Setter
@Getter
@NoArgsConstructor
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
    @Column("deviceId")
    private String deviceId;
    private Boolean status;

}
