package com.agritechiot.agritech_iot.dto;

import com.agritechiot.agritech_iot.constant.GenConstant;
import com.agritechiot.agritech_iot.model.IoTDevice;
import lombok.Data;
import org.slf4j.MDC;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.List;

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


    public ApiResponse() {
        this.code = GenConstant.SUC_CODE;
        this.timestamp = LocalDateTime.now();
        this.correlationId = MDC.get(GenConstant.CORRELATION_ID);
        this.message = GenConstant.SUC_MESSAGE;
        this.data = null;
    }
}