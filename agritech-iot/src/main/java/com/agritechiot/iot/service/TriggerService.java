package com.agritechiot.iot.service;

import com.agritechiot.iot.dto.request.TriggerReq;
import com.agritechiot.iot.model.Trigger;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TriggerService {
    Flux<Trigger> getTriggerBySensorAndDeviceId(String sensor, String deviceId);

    Flux<Trigger> saveMultipleTriggers(TriggerReq req);

    Mono<Trigger> saveTrigger(Trigger req);

    Mono<Trigger> updateTrigger(Integer id, Trigger req);

    Flux<Trigger> getTriggers();

    Flux<String> getSensors();

    Mono<Void> softDeleteById(Integer id);

    Mono<Trigger> getTriggerByDeviceId(String deviceId);
}
