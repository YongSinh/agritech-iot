package com.agritechiot.iot.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
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
@Table(name = "tbl_interval_schedule")
public class IntervalSchedule {
    @Id
    private Integer id;
    @Column("interval_minutes")
    private Integer interval;
    @Column("turnOn_water")
    private Boolean turnOnWater;
    @Column("duration")
    private Integer duration;
    @Column("read_sensor")
    private Boolean readSensor;
    @Column("deviceId")
    private String deviceId;
    @Column("run_datetime")
    private LocalDateTime runDatetime;
    private Boolean status;
    @Column("isRemoved")
    private Boolean isRemoved;
    @Column("deletedAt")
    private LocalDateTime deletedAt;
}
