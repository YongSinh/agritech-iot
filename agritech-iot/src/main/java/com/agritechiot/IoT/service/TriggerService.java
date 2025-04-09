package com.agritechiot.IoT.service;

import com.agritechiot.IoT.model.Trigger;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TriggerService {
    Flux<Trigger> getTriggerBySensorAndDeviceId(String sensor, String deviceId);

    Mono<Trigger> saveTrigger(Trigger req);

    Mono<Trigger> updateTrigger(Integer id, Trigger req);

    Flux<Trigger> getTriggers();

}
