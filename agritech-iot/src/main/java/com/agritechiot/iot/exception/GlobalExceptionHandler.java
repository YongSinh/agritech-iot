package com.agritechiot.iot.exception;

import com.agritechiot.iot.dto.ApiErrResponse;
import com.agritechiot.iot.dto.ApiResponse;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestControllerAdvice
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ApiResponse<String>>> runtimeException(Exception ex, ServerWebExchange exchange) {
        return Mono.just(new ResponseEntity<>(new ApiResponse<>(ex.getMessage()), HttpStatus.CONFLICT));
    }

    @ExceptionHandler(InvalidDefinitionException.class)
    public Mono<ResponseEntity<ApiResponse<String>>> invalidDefinitionException(InvalidDefinitionException ex) {
        return Mono.just(new ResponseEntity<>(new ApiResponse<>(ex.getMessage()), HttpStatus.CONFLICT));
    }

    @ExceptionHandler(NullPointerException.class)
    public Mono<ResponseEntity<ApiErrResponse<String>>> nullPointerException(NullPointerException ex) {
        return Mono.just(new ResponseEntity<>(new ApiErrResponse<>(ex.getMessage()), HttpStatus.BAD_REQUEST));
    }

}
