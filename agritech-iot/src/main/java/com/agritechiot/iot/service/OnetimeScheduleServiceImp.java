package com.agritechiot.iot.service;

import com.agritechiot.iot.constant.GenConstant;
import com.agritechiot.iot.dto.request.OnetimeScheduleReq;
import com.agritechiot.iot.dto.response.ActiveScheduleRes;
import com.agritechiot.iot.model.IoTDevice;
import com.agritechiot.iot.model.OnetimeSchedule;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class OnetimeScheduleServiceImp implements OnetimeScheduleService {
    private final OnetimeScheduleRepo onetimeScheduleRepo;
    private final Publisher publisher;
    private final IoTDeviceRepo ioTDeviceRepo;
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
                    return onetimeSchedule;
                }).flatMap(onetimeScheduleRepo::save);
    }

    @Override
    public Flux<OnetimeSchedule> getListOnetimeSchedule() {
        return onetimeScheduleRepo.findAll();
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
    public void startOneTimeSchedule(OnetimeScheduleReq req) throws Exception {
        log.info("Reading sensors one time Schedule for device {} at {}", req.getDeviceId(), LocalDateTime.now());
        log.info(JsonUtil.objectToJsonString(req));
        IoTDevice ioTDevice = ioTDeviceRepo.findById(req.getDeviceId()).block();
        assert ioTDevice != null;
        log.info(ioTDevice.getName());
        publisher.publish(ioTDevice.getName(), JsonUtil.objectToJsonString(req), 1, true);
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


    public void checkAndExecuteSchedules() {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now().withSecond(0).withNano(0);
        Flux<OnetimeSchedule> schedules = onetimeScheduleRepo.findByDate(today);
        log.info("Schedules start at: {}", LocalDateTime.now());
        // Process schedules reactively
        schedules
                .doOnNext(schedule -> log.info("Schedule found: {}", schedule)) // Log each schedule
                .doOnComplete(() -> log.info("No more schedules found for today: {}", today)) // Log when the stream completes
                .filter(schedule -> schedule.getTime().equals(now)) // Filter schedules matching the current time
                .subscribe(schedule -> {
                    // Execute tasks based on the schedule
                    if (Boolean.TRUE.equals(schedule.getReadSensor())) {
                        readSensor(schedule.getDeviceId());
                    }
                    if (Boolean.TRUE.equals(schedule.getTurnOnWater())) {
                        turnOnWater(schedule.getDeviceId(), schedule.getDuration());
                    }
                }, error -> log.error("Error processing schedules: {}", error.getMessage())); // Log
    }

    private void readSensor(String deviceId) {
        // Logic to read sensor data
        log.info("Reading sensor for device: {}", deviceId);
    }

    private void turnOnWater(String deviceId, Integer duration) {
        // Logic to turn on water for the specified duration
        log.info("Turning on water for device: {} for {} minutes", deviceId, duration);
    }
}
