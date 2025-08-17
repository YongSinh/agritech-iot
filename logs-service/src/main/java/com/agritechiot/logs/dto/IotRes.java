package com.agritechiot.logs.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IotRes<T> {
    private String code;
    private LocalDateTime timestamp;
    private String message;
    private String correlationId;
    private T data;
}