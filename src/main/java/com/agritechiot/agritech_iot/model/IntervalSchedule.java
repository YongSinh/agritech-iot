package com.agritechiot.agritech_iot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
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
    private Boolean read_sensor;
    private Boolean turnOn_water;
    private Integer duration;
    //@JsonProperty("device_id")
    private String deviceid;
    private Integer run_datetime;
}
