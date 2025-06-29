package com.agritechiot.iot.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@Setter
@Table(name = "mqtt_topic")
@ToString
public class MqttTopic {
    @Id
    private Integer id;
    private String topic;
    @Column("createdBy")
    private String createdBy;
    @Column("isRemoved")
    private Boolean isRemoved;
    @Column("deletedAt")
    private LocalDateTime deletedAt;
}
