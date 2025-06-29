package com.agritechiot.iot.dto;

import com.agritechiot.iot.constant.GenConstant;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Getter
@Setter
@ToString
public class ErrorResponse<T> {
    private String code;
    private String message;
    private String path;
    private Instant timestamp;
    private T data;

    public ErrorResponse(String message, String path) {
        this.code = GenConstant.ERR_CODE;
        this.message = message;
        this.path = path;
        this.timestamp = Instant.now();
        this.data = null;
    }
}
