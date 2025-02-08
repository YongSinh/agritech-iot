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
        return triggerRepo.findByDeviceIdAndSensor(deviceId, sensor);
    }

    @Override
    public Mono<Trigger> saveTrigger(Trigger req) {
        Trigger trigger = new Trigger();
        trigger.setAction(req.getAction());
        trigger.setDatetime(req.getDatetime());
        trigger.setOperator(req.getOperator());
        trigger.setDuration(req.getDuration());
        trigger.setSensor(req.getSensor());
        trigger.setValue(req.getValue());
        trigger.setDeviceId(req.getDeviceId());
        return triggerRepo.save(trigger);
    }

    @Override
    public Mono<Trigger> updateTrigger(Integer id, Trigger req) {
        return triggerRepo.findById(id)
                .switchIfEmpty(Mono.error(new Exception("TRIGGER_NOT_FOUND")))
                .map(trigger -> {
                    trigger.setId(id);
                    return trigger;
                }).flatMap(triggerRepo::save);

    }

}
