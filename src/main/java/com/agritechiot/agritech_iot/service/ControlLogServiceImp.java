package com.agritechiot.agritech_iot.service;

import com.agritechiot.agritech_iot.repository.ControlLogRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ControlLogServiceImp implements ControlLogService {
    private final ControlLogRepo controlLogRepo;
}
