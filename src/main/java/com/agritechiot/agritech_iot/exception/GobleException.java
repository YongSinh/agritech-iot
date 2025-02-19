package com.agritechiot.agritech_iot.exception;

import com.agritechiot.agritech_iot.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestControllerAdvice
@ControllerAdvice
public class GobleException {
//    @ExceptionHandler(BadRequestException.class)
//    public Mono<ResponseEntity<String>> handleBadRequestException(BadRequestException ex, ServerWebExchange exchange) {
//        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage()));
//    }
//
//    @ExceptionHandler(Exception.class)
//    public Mono<ApiResponse<Object>> handleGenericException(Exception ex, ServerWebExchange exchange) {
//        return Mono.just(new ApiResponse<>(ex.getMessage()));
//    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<?>> runtimeException(Exception ex, ServerWebExchange exchange) {
        return Mono.just(new ResponseEntity<>(new ApiResponse<>(ex.getMessage()), HttpStatus.CONFLICT));
    }

}
