package com.agritechiot.logs.dto;

import com.agritechiot.logs.constant.GenConstant;
import lombok.Data;
import org.slf4j.MDC;

import java.time.LocalDateTime;

@Data
public class ApiResponse<T> {
    private String code;
    private LocalDateTime timestamp;
    private String message;
    private String correlationId;
    private T data;

    public ApiResponse(T data) {
        this.code = GenConstant.SUC_CODE;
        this.timestamp = LocalDateTime.now();
        this.correlationId = MDC.get(GenConstant.CORRELATION_ID);
        this.message = GenConstant.SUC_MESSAGE;
        this.data = data;
    }

    public ApiResponse(T data, String correlationId) {
        this.code = GenConstant.SUC_CODE;
        this.timestamp = LocalDateTime.now();
        this.correlationId = correlationId != null ? correlationId : GenConstant.DEFAULT_CORRELATION_ID;
        this.message = GenConstant.SUC_MESSAGE;
        this.data = data;
    }

    public ApiResponse(String message, String correlationId) {
        this.code = GenConstant.ERR_CODE;
        this.timestamp = LocalDateTime.now();
        this.correlationId = correlationId != null ? correlationId : GenConstant.DEFAULT_CORRELATION_ID;
        this.message = message;
        this.data = null;
    }

    public ApiResponse() {
        this.code = GenConstant.SUC_CODE;
        this.timestamp = LocalDateTime.now();
        this.correlationId = MDC.get(GenConstant.CORRELATION_ID);
        this.message = GenConstant.SUC_MESSAGE;
        this.data = null;
    }
}