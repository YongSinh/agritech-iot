package com.agritechiot.IoT.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MqttPublishReq {
    @NotNull
    @Size(min = 1, max = 255)
    private String topic;
    @NotNull
    private Object message; // Changed from String to Object
    @NotNull
    private Boolean retained;
    @NotNull
    private Integer qos;
}
