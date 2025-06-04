package com.agritechiot.iot.service;

import com.agritechiot.iot.constant.GenConstant;
import com.agritechiot.iot.dto.request.OnetimeScheduleReq;
import com.agritechiot.iot.dto.response.ActiveScheduleRes;
import com.agritechiot.iot.model.IoTDevice;
import com.agritechiot.iot.model.OnetimeSchedule;
import com.agritechiot.iot.model.Trigger;
import com.agritechiot.iot.repository.IoTDeviceRepo;
import com.agritechiot.iot.repository.OnetimeScheduleRepo;
import com.agritechiot.iot.service.mqtt.Publisher;
import com.agritechiot.iot.util.GenUtil;
import com.agritechiot.iot.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class OnetimeScheduleServiceImp implements OnetimeScheduleService {
    private final OnetimeScheduleRepo onetimeScheduleRepo;
    private final Publisher publisher;
    private final IoTDeviceRepo ioTDeviceRepo;
    private final TriggerService triggerService;
    private final LogService logService;

    @Override
    public Mono<OnetimeSchedule> saveOnetimeSchedule(OnetimeScheduleReq req) {
        OnetimeSchedule onetimeSchedule = new OnetimeSchedule();
        onetimeSchedule.setDuration(req.getDuration());
        onetimeSchedule.setDeviceId(req.getDeviceId());
        onetimeSchedule.setTime(GenUtil.parsedTime(req.getTime()));
        onetimeSchedule.setReadSensor(req.getReadSensor());
        onetimeSchedule.setDate(req.getDate());
        onetimeSchedule.setTurnOnWater(req.getTurnOnWater());
        onetimeSchedule.setIsRemoved(false);
        return onetimeScheduleRepo.save(onetimeSchedule);
    }

    @Override
    public Mono<OnetimeSchedule> updateOnetimeSchedule(Integer id, OnetimeScheduleReq req) {
        return onetimeScheduleRepo.findById(id)
                .switchIfEmpty(Mono.error(new Exception("ONE_TIME_SCHEDULE_NOT_FOUND")))
                .map(onetimeSchedule -> {
                    onetimeSchedule.setId(id);
                    onetimeSchedule.setDuration(req.getDuration());
                    onetimeSchedule.setDeviceId(req.getDeviceId());
                    onetimeSchedule.setTime(GenUtil.parsedTime(req.getTime()));
                    onetimeSchedule.setReadSensor(req.getReadSensor());
                    onetimeSchedule.setDate(req.getDate());
                    onetimeSchedule.setTurnOnWater(req.getTurnOnWater());
                    onetimeSchedule.setIsRemoved(false);
                    return onetimeSchedule;
                }).flatMap(onetimeScheduleRepo::save);
    }

    @Override
    public Flux<OnetimeSchedule> getListOnetimeSchedule() {
        return onetimeScheduleRepo.findByIsNotDeleted();
    }

    @Override
    public Flux<OnetimeSchedule> getListOnetimeScheduleDuration(Integer duration, Integer duration2) {
        return onetimeScheduleRepo.findAllByDurationBetween(duration, duration2);
    }

    @Override
    public Flux<OnetimeSchedule> getListOnetimeScheduleByDeviceId(String deviceId) {
        return onetimeScheduleRepo.findByDeviceId(deviceId);
    }

    @Override
    public void startOneTimeSchedule(OnetimeSchedule req) throws Exception {
        log.info("Reading sensors one time Schedule for device {} at {}", req.getDeviceId(), LocalDateTime.now());
        IoTDevice ioTDevice = ioTDeviceRepo.findById(req.getDeviceId()).block();
        Trigger trigger = triggerService.getTriggerByDeviceId(req.getDeviceId()).block();
        assert ioTDevice != null;
        publisher.publish(ioTDevice.getName(), JsonUtil.objectToJsonString(trigger), 1, true);
    }

    @Override
    public Mono<Void> updateListsStatus(List<Integer> ids, boolean newStatus, int batchSize) {
        if (ids == null || ids.isEmpty()) {
            return Mono.error(new IllegalArgumentException("IDs list cannot be null or empty"));
        }
        return Flux.fromIterable(ids)
                .buffer(batchSize)
                .flatMap(batch -> onetimeScheduleRepo.updateStatusForIds(batch, newStatus))
                .then();
    }

    @Override
    public Mono<Void> updateSingleStatus(Integer id, boolean newStatus) {
        log.info("Updating single status by id {}", id);
        return onetimeScheduleRepo.updateStatusById(id, newStatus)
                .then();
    }

    @Override
    public Mono<ActiveScheduleRes> getActiveOnetimeSchedule() {
        return onetimeScheduleRepo.countActiveOnetimeStatus()
                .map(count -> {
                    ActiveScheduleRes res = new ActiveScheduleRes();
                    res.setActiveSchedule(count);
                    res.setScheduleType(GenConstant.ONETIME_SCHEDULE_TYPE);
                    return res;
                });
    }

    @Override
    public Mono<Void> softDeleteById(Integer id) {
        return onetimeScheduleRepo.findById(id)
                .switchIfEmpty(Mono.error(new Exception("NOT_FOUND")))
                .flatMap(req -> {
                    req.setIsRemoved(true);
                    req.setDeletedAt(LocalDateTime.now());
                    return onetimeScheduleRepo.save(req);
                })
                .then();
    }

}
