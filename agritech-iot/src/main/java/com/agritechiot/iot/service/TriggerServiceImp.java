package com.agritechiot.iot.service;

import com.agritechiot.iot.model.Trigger;
import com.agritechiot.iot.repository.TriggerRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class TriggerServiceImp implements TriggerService {
    private final TriggerRepo triggerRepo;

    @Override
    public Flux<Trigger> getTriggerBySensorAndDeviceId(String sensor, String deviceId) {
        return triggerRepo.findByDeviceIdAndSensor(deviceId, sensor);
    }

    @Override
    public Mono<Trigger> saveTrigger(Trigger req) {
        log.info("REQ_SAVE_TRIGGER_SERVICE: {}", req);
        Trigger trigger = new Trigger();
        trigger.setAction(req.getAction());
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
                    trigger.setAction(req.getAction());
                    trigger.setOperator(req.getOperator());
                    trigger.setDuration(req.getDuration());
                    trigger.setSensor(req.getSensor());
                    trigger.setValue(req.getValue());
                    trigger.setDeviceId(req.getDeviceId());
                    return trigger;
                }).flatMap(triggerRepo::save);

    }

    @Override
    public Flux<Trigger> getTriggers() {
        return triggerRepo.findAll();
    }

}
