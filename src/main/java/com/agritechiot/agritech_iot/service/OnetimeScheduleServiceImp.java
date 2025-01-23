package com.agritechiot.agritech_iot.service;

import com.agritechiot.agritech_iot.repository.IntervalScheduleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OnetimeScheduleServiceImp implements OnetimeScheduleService {
    private final IntervalScheduleRepo intervalScheduleRepo;
}
