package com.agritechiot.agritech_iot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

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
    private Boolean read_sensor;
    private Boolean turnOn_water;
    private Integer duration;
    @JsonProperty("device_id")
    private String deviceid;
}
