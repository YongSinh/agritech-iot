package com.agritechiot.iot.exception;

import com.agritechiot.iot.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse<Void>> handleGeneric(Exception ex, HttpServletRequest request) {
        ErrorResponse<Void> error = new ErrorResponse<>(
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponse<Void>> handleAppException(AppException ex, HttpServletRequest request) {
        ErrorResponse<Void> error = new ErrorResponse<>(
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }


}
