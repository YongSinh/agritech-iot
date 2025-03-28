package com.agritechiot.agritech_iot.service;

import com.agritechiot.agritech_iot.dto.request.RepeatScheduleReq;
import com.agritechiot.agritech_iot.model.RepeatSchedule;
import com.agritechiot.agritech_iot.repository.RepeatScheduleRepo;
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
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@Service
@Slf4j
public class RepeatScheduleServiceImp implements RepeatScheduleService {
    private final RepeatScheduleRepo repeatScheduleRepo;

    @Override
    public Mono<RepeatSchedule> saveRepeatSchedule(RepeatScheduleReq req) {
        RepeatSchedule repeatSchedule = new RepeatSchedule();
        repeatSchedule.setTime(GenUtil.parsedTime(req.getTime()));
        repeatSchedule.setDeviceId(req.getDeviceId());
        repeatSchedule.setDuration(req.getDuration());
        repeatSchedule.setDay(req.getDay().toUpperCase());
        repeatSchedule.setReadSensor(req.getReadSensor());
        repeatSchedule.setTurnOnWater(req.getTurnOnWater());
        return repeatScheduleRepo.save(repeatSchedule);
    }

    @Override
    public Mono<RepeatSchedule> updateRepeatSchedule(Integer id, RepeatScheduleReq req) {
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
                    return repeatSchedule;
                }).flatMap(repeatScheduleRepo::save);
    }

    @Override
    public Flux<RepeatSchedule> getListRepeatSchedule() {
        return repeatScheduleRepo.findAll();
    }

    @Override
    public Flux<RepeatSchedule> getListRepeatScheduleByDay(String day) {
        return repeatScheduleRepo.findByDay(day);
    }

    @Override
    public Flux<RepeatSchedule> getListRepeatScheduleByDeviceId(String deviceId) {
        return repeatScheduleRepo.findByDeviceId(deviceId);
    }

    // Run this method every minute
    @Scheduled(fixedRate = 60000) // 60000 ms = 1 minute
    public void checkAndExecuteRepeatSchedules() {
        String today = LocalDate.now().getDayOfWeek().name().toUpperCase();
        LocalTime now = LocalTime.now().withSecond(0).withNano(0);

        log.info("Checking repeat schedules at: {}", now);

        repeatScheduleRepo.findByDay(today)
                .doOnNext(schedule -> log.info("Found schedule: {}", schedule))
                .doOnComplete(() -> log.info("No more schedules for {}", today))
                // Check if current time is within 1 hour after the scheduled time
                .filter(schedule -> {
                    LocalTime scheduleTime = schedule.getTime(); // e.g., 22:00:00
                    LocalTime oneHourAfterSchedule = scheduleTime.plusHours(1);

                    // Current time is >= scheduleTime AND < oneHourAfterSchedule
                    return !now.isBefore(scheduleTime) && now.isBefore(oneHourAfterSchedule);
                })
                .subscribe(schedule -> {
                    if (Boolean.TRUE.equals(schedule.getReadSensor())) {
                        readSensor(schedule.getDeviceId());
                    }
                    if (Boolean.TRUE.equals(schedule.getTurnOnWater())) {
                        turnOnWater(schedule.getDeviceId(), schedule.getDuration());
                    }
                }, error -> log.error("Error: {}", error.getMessage()));
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
