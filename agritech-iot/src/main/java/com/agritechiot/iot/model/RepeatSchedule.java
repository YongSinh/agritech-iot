package com.agritechiot.iot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Setter
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@Table("tbl_repeat_schedule")
public class RepeatSchedule {
    @Id
    private Integer id;
    private String day;
    private LocalTime time;
    @Column("read_sensor")
    private Boolean readSensor;
    @Column("turnOn_water")
    private Boolean turnOnWater;
    private Integer duration;
    @Column("deviceId")
    private String deviceId;
    private Boolean status;
    @Column("isRemoved")
    private Boolean isRemoved;
    @Column("deletedAt")
    private LocalDateTime deletedAt;

}
