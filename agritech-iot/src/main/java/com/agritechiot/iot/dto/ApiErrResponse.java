package com.agritechiot.iot.dto;

import com.agritechiot.iot.constant.GenConstant;
import lombok.Data;
import org.slf4j.MDC;

import java.time.LocalDateTime;

@Data
public class ApiErrResponse<T> {
    private String code;
    private LocalDateTime timestamp;
    private String message;
    private String correlationId;
    private T data;

    public ApiErrResponse() {
        this(null, MDC.get(GenConstant.CORRELATION_ID), GenConstant.SUC_MESSAGE, GenConstant.SUC_CODE);
    }

    public ApiErrResponse(T data) {
        this(data, MDC.get(GenConstant.CORRELATION_ID), GenConstant.ERR_MESSAGE, GenConstant.ERR_CODE);
    }

    public ApiErrResponse(T data, String correlationId) {
        this(data, correlationId, GenConstant.SUC_MESSAGE, GenConstant.SUC_CODE);
    }

    public ApiErrResponse(String message, String correlationId) {
        this(null, correlationId, message, GenConstant.ERR_CODE);
    }

    public ApiErrResponse(String message, String correlationId, String code) {
        this(null, correlationId, message, code);
    }

    private ApiErrResponse(T data, String correlationId, String message, String code) {
        this.code = code;
        this.timestamp = LocalDateTime.now();
        this.correlationId = correlationId != null ? correlationId : GenConstant.DEFAULT_CORRELATION_ID;
        this.message = message;
        this.data = data;
    }
}