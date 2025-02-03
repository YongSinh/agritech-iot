package com.agritechiot.agritech_iot.service;

import com.agritechiot.agritech_iot.model.Trigger;
import com.agritechiot.agritech_iot.repository.TriggerRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TriggerServiceImp implements TriggerService {
    private final TriggerRepo triggerRepo;

    @Override
    public Mono<Trigger> getTriggerBySensorAndDeviceId(String sensor, String deviceId) {
        return triggerRepo.findByDeviceIdAndSensor(deviceId,sensor);
    }
}
