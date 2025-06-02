package com.agritechiot.iot.service;

import com.agritechiot.iot.constant.GenConstant;
import com.agritechiot.iot.dto.request.RepeatScheduleReq;
import com.agritechiot.iot.dto.response.ActiveScheduleRes;
import com.agritechiot.iot.model.IoTDevice;
import com.agritechiot.iot.model.RepeatSchedule;
import com.agritechiot.iot.model.Trigger;
import com.agritechiot.iot.repository.IoTDeviceRepo;
import com.agritechiot.iot.repository.RepeatScheduleRepo;
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
public class RepeatScheduleServiceImp implements RepeatScheduleService {
    private final RepeatScheduleRepo repeatScheduleRepo;
    private final Publisher publisher;
    private final IoTDeviceRepo ioTDeviceRepo;
    private final LogService logService;
    private final TriggerService triggerService;

    @Override
    public Mono<RepeatSchedule> saveRepeatSchedule(RepeatScheduleReq req) {
        logService.logInfo("REQ_IOT_CREATE_REPEAT_SCHEDULE", JsonUtil.toJson(req));
        RepeatSchedule repeatSchedule = new RepeatSchedule();
        repeatSchedule.setTime(GenUtil.parsedTime(req.getTime()));
        repeatSchedule.setDeviceId(req.getDeviceId());
        repeatSchedule.setDuration(req.getDuration());
        repeatSchedule.setDay(req.getDay().toUpperCase());
        repeatSchedule.setReadSensor(req.getReadSensor());
        repeatSchedule.setTurnOnWater(req.getTurnOnWater());
        repeatSchedule.setIsRemoved(false);
        return repeatScheduleRepo.save(repeatSchedule);
    }

    @Override
    public Mono<RepeatSchedule> updateRepeatSchedule(Integer id, RepeatScheduleReq req) {
        logService.logInfo("REQ_IOT_UPDATE_REPEAT_SCHEDULE", JsonUtil.toJson(req));
        return repeatScheduleRepo.findById(id)
                .switchIfEmpty(Mono.error(new Exception("ONE_TIME_SCHEDULE_NOT_FOUND")))
                .map(repeatSchedule -> {
                    repeatSchedule.setId(id);
                    repeatSchedule.setTime(GenUtil.parsedTime(req.getTime()));
                    repeatSchedule.setDeviceId(req.getDeviceId());
                    repeatSchedule.setDuration(req.getDuration());
                    repeatSchedule.setDay(req.getDay().toUpperCase());
                    repeatSchedule.setReadSensor(req.getReadSensor());
                    repeatSchedule.setTurnOnWater(req.getTurnOnWater());
                    repeatSchedule.setIsRemoved(false);
                    return repeatSchedule;
                }).flatMap(repeatScheduleRepo::save);
    }

    @Override
    public Flux<RepeatSchedule> getListRepeatSchedule() {
        logService.logInfo("REQ_IOT_REPEAT_SCHEDULES");
        return repeatScheduleRepo.findByIsNotDeleted();
    }

    @Override
    public void startRepeatSchedule(RepeatSchedule repeatSchedule) throws Exception {
        log.info("Reading sensors for device {} at {}", repeatSchedule.getDeviceId(), LocalDateTime.now());
        log.info(JsonUtil.objectToJsonString(repeatSchedule));
        IoTDevice ioTDevice = ioTDeviceRepo.findById(repeatSchedule.getDeviceId()).block();
        Trigger trigger = triggerService.getTriggerByDeviceId(repeatSchedule.getDeviceId()).block();
        assert ioTDevice != null;
        log.info(ioTDevice.getName());
        publisher.publish(ioTDevice.getName(), JsonUtil.objectToJsonString(trigger), 1, true);
    }

    @Override
    public Flux<RepeatSchedule> getListRepeatScheduleByDay(String day) {
        logService.logInfo("REQ_IOT_REPEAT_SCHEDULE_BY_DAY", day);
        return repeatScheduleRepo.findByDay(day);
    }

    @Override
    public Flux<RepeatSchedule> getListRepeatScheduleByDeviceId(String deviceId) {
        logService.logInfo("REQ_IOT_REPEAT_SCHEDULE_DEVICE_ID", deviceId);
        return repeatScheduleRepo.findByDeviceId(deviceId);
    }


    @Override
    public Mono<Void> updateListsStatus(List<Integer> ids, boolean newStatus, int batchSize) {
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("IDs list cannot be null or empty");
        }
        return Flux.fromIterable(ids)
                .buffer(batchSize)
                .flatMap(batch -> repeatScheduleRepo.updateStatusForIds(batch, newStatus))
                .then();
    }

    @Override
    public Mono<Void> updateSingleStatus(Integer id, boolean newStatus) {
        log.info("Updating single status by id {}", id);
        return repeatScheduleRepo.updateStatusById(id, newStatus)
                .then();
    }

    @Override
    public Mono<ActiveScheduleRes> getActiveRepeatSchedule() {
        return repeatScheduleRepo.countActiveRepeatStatus()
                .map(count -> {
                    ActiveScheduleRes res = new ActiveScheduleRes();
                    res.setActiveSchedule(count);
                    res.setScheduleType(GenConstant.REPEAT_SCHEDULE_TYPE);
                    return res;
                });
    }

    @Override
    public Mono<Void> softDeleteById(Integer id) {
        return repeatScheduleRepo.findById(id)
                .switchIfEmpty(Mono.error(new Exception("NOT_FOUND")))
                .flatMap(req -> {
                    req.setIsRemoved(true);
                    req.setDeletedAt(LocalDateTime.now());
                    return repeatScheduleRepo.save(req);
                })
                .then();
    }

}
