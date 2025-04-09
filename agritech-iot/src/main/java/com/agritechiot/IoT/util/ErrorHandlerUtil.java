package com.agritechiot.IoT.util;

import com.agritechiot.IoT.dto.ApiResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ErrorHandlerUtil {

    private ErrorHandlerUtil() {
        // Private constructor to prevent instantiation
    }

    public static <T> Mono<ApiResponse<T>> handleDuplicateError(Throwable error, String message, String correlationId, String entity) {
        String messageRes;
        if (error instanceof DuplicateKeyException) {
            messageRes = message;
        } else if (error instanceof DataIntegrityViolationException) {
            messageRes = "Invalid data provided for the" + entity + ".";
        } else {
            messageRes = "Failed to process the request: " + error.getMessage();
        }

        return Mono.just(new ApiResponse<>(messageRes, correlationId));
    }
}
