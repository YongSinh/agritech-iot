package com.agritechiot.iot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@Setter
@Table(name = "control_log")
@AllArgsConstructor
@RequiredArgsConstructor
public class ControlLog {
    @Id
    private Integer id;
    @Column("datetime")
    private LocalDateTime dateTime;
    @Column("deviceId")
    private String deviceId;
    private Boolean status;
    private Integer duration;
    @Column("sentby")
    private String sentBy;
    @Column("isRemoved")
    private Boolean isRemoved;
    @Column("deletedAt")
    private LocalDateTime deletedAt;
}
