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
    public Flux<RepeatSchedule> getListRepeatScheduleByDeviceId(String deviceId) {
        return repeatScheduleRepo.findByDeviceId(deviceId);
    }

    // Run this method every minute
    @Scheduled(fixedRate = 60000) // 60000 ms = 1 minute
    public void checkAndExecuteRepeatSchedules() {
        String today = LocalDate.now().getDayOfWeek().name().toLowerCase();  // Get current day of the week (e.g., "MONDAY")
        LocalTime now = LocalTime.now().withSecond(0).withNano(0); // Ignore seconds and nanoseconds
        log.info("Repeat schedule start at: {}", LocalDateTime.now());
        // Fetch schedules for today
        Flux<RepeatSchedule> schedules = repeatScheduleRepo.findByDay(today);

        // Log the schedules found (or if empty)
        schedules
                .doOnNext(schedule -> log.info("Repeat schedule found: {}", schedule)) // Log each schedule
                .doOnComplete(() -> log.info("No more repeat schedules found for today: {}", today)) // Log when the stream completes
                .filter(schedule -> schedule.getTime().equals(now)) // Filter schedules matching the current time
                .subscribe(schedule -> {
                    // Execute tasks based on the schedule
                    if (Boolean.TRUE.equals(schedule.getReadSensor())) {
                        readSensor(schedule.getDeviceId());
                    }
                    if (Boolean.TRUE.equals(schedule.getTurnOnWater())) {
                        turnOnWater(schedule.getDeviceId(), schedule.getDuration());
                    }
                }, error -> log.error("Error processing repeat schedules: {}", error.getMessage())); // Log errors
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
