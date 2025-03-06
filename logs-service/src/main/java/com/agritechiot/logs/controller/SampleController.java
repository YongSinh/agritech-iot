package com.agritechiot.logs.controller;

import com.agritechiot.logs.constant.GenConstant;
import com.agritechiot.logs.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class SampleController {

    @PostMapping("/sample")
    public ResponseEntity<?> cbsAccountInfo(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId) {
        return ResponseEntity.ok(new ApiResponse<>());
    }

}
