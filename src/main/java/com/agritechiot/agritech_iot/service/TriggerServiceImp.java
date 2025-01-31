package com.agritechiot.agritech_iot.service;

import com.agritechiot.agritech_iot.repository.TriggerRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TriggerServiceImp implements TriggerService {
    private final TriggerRepo triggerRepo;
}
