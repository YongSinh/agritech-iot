package com.agritechiot.agritech_iot.service;

import com.agritechiot.agritech_iot.dto.request.OnetimeScheduleReq;
import com.agritechiot.agritech_iot.model.OnetimeSchedule;
import com.agritechiot.agritech_iot.repository.OnetimeScheduleRepo;
import com.agritechiot.agritech_iot.util.GenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RequiredArgsConstructor
@Service
@Slf4j
public class OnetimeScheduleServiceImp implements OnetimeScheduleService {
    private final OnetimeScheduleRepo onetimeScheduleRepo;

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

    @Scheduled(fixedRate = 60000) // 60000 ms = 1 minute
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
