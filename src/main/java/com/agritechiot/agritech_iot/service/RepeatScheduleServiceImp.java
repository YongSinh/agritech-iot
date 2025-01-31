package com.agritechiot.agritech_iot.service;

import com.agritechiot.agritech_iot.repository.RepeatScheduleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RepeatScheduleServiceImp implements RepeatScheduleService {
    private final RepeatScheduleRepo repeatScheduleRepo;
}
