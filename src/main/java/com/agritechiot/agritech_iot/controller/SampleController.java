package com.agritechiot.agritech_iot.controller;

import com.agritechiot.agritech_iot.constant.GenConstant;
import com.agritechiot.agritech_iot.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class SampleController {

    @PostMapping("/sample")
    public ResponseEntity<?> cbsAccountInfo(
            @RequestHeader(value = GenConstant.CORRELATION_ID, required = false) String correlationId) {
        return ResponseEntity.ok(new ApiResponse<>());
    }

}
