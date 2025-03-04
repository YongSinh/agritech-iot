package com.agritechiot.api_gateway;

import com.agritechiot.api_gateway.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FallbackController {
    @RequestMapping("/fallback")
    public Mono<ResponseEntity<ApiResponse<String>>> fallback() {
        ApiResponse<String> response = new ApiResponse<>("Service is temporarily unavailable. Please try again later.");
        return Mono.just(ResponseEntity.status(HttpStatus.OK).body(response));
    }
}
