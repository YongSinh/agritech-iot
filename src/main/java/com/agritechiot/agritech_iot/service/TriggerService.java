package com.agritechiot.agritech_iot.service;

import com.agritechiot.agritech_iot.model.Trigger;
import reactor.core.publisher.Mono;

public interface TriggerService {
    Mono<Trigger> getTriggerBySensorAndDeviceId(String sensor, String deviceId);
}
