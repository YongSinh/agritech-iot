package com.agritechiot.iot.service;

import com.agritechiot.iot.dto.request.TriggerReq;
import com.agritechiot.iot.model.Trigger;
import com.agritechiot.iot.repository.TriggerRepo;
import com.agritechiot.iot.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TriggerServiceImp implements TriggerService {
    private final TriggerRepo triggerRepo;
    private final LogService logService;
    @Value("${sensor.value}")
    private String[] sensors;

    @Override
    public Flux<Trigger> getTriggerBySensorAndDeviceId(String sensor, String deviceId) {
        return triggerRepo.findByDeviceIdAndSensor(deviceId, sensor);
    }

    @Override
    public Flux<Trigger> saveMultipleTriggers(TriggerReq req) {
        logService.logInfo("REQ_SAVE_MULTIPLE_TRIGGERS_SERVICE: ", JsonUtil.toJson(req));
        List<Trigger> triggers = req.getDeviceId().stream().map(deviceId -> {
            Trigger trigger = new Trigger();
            trigger.setAction(req.getAction());
            trigger.setOperator(req.getOperator());
            trigger.setDuration(req.getDuration());
            trigger.setSensor(req.getSensor());
            trigger.setValue(req.getValue());
            trigger.setDeviceId(deviceId); // set different deviceId
            trigger.setIsRemoved(false);
            return trigger;
        }).toList();

        return triggerRepo.saveAll(triggers);
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
        trigger.setIsRemoved(false);
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
                    trigger.setIsRemoved(false);
                    return trigger;
                }).flatMap(triggerRepo::save);

    }

    @Override
    public Flux<Trigger> getTriggers() {
        return triggerRepo.findByIsNotDeleted();
    }

    @Override
    public Flux<String> getSensors() {
        return Flux.fromArray(sensors);
    }

    @Override
    public Mono<Void> softDeleteById(Integer id) {
        return triggerRepo.findById(id)
                .switchIfEmpty(Mono.error(new Exception("NOT_FOUND")))
                .flatMap(req -> {
                    req.setIsRemoved(true);
                    req.setDeletedAt(LocalDateTime.now());
                    return triggerRepo.save(req);
                })
                .then();
    }

    @Override
    public Mono<Trigger> getTriggerByDeviceId(String deviceId) {
        return triggerRepo.findByDeviceId(deviceId)
                .switchIfEmpty(Mono.error(new Exception("NOT_FOUND")));
    }

}
