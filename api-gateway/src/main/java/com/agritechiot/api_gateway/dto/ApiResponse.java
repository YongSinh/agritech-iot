package com.agritechiot.api_gateway.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)  // Ignore null values in JSON response
public class ApiResponse<T> {

    @JsonProperty("code")
    private String code;

    @JsonProperty("timestamp")
    private String timestamp;

    @JsonProperty("message")
    private String message;

    @JsonProperty("correlationId")
    private String correlationId;

    @JsonProperty("data")
    private T data;

    public ApiResponse(String message) {
        this.code = "ERR-001";
        this.timestamp = LocalDateTime.now().toString();
        this.correlationId = UUID.randomUUID().toString();
        this.message = message;
        this.data = null;
    }
}