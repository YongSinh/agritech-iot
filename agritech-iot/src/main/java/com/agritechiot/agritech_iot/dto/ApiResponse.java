package com.agritechiot.agritech_iot.dto;

import com.agritechiot.agritech_iot.constant.GenConstant;
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

    public ApiResponse() {
        this(null, MDC.get(GenConstant.CORRELATION_ID), GenConstant.SUC_MESSAGE, GenConstant.SUC_CODE);
    }

    public ApiResponse(T data) {
        this(data, MDC.get(GenConstant.CORRELATION_ID), GenConstant.SUC_MESSAGE, GenConstant.SUC_CODE);
    }

    public ApiResponse(T data, String correlationId) {
        this(data, correlationId, GenConstant.SUC_MESSAGE, GenConstant.SUC_CODE);
    }

    public ApiResponse(String message, String correlationId) {
        this(null, correlationId, message, GenConstant.ERR_CODE);
    }

    private ApiResponse(T data, String correlationId, String message, String code) {
        this.code = code;
        this.timestamp = LocalDateTime.now();
        this.correlationId = correlationId != null ? correlationId : GenConstant.DEFAULT_CORRELATION_ID;
        this.message = message;
        this.data = data;
    }
}