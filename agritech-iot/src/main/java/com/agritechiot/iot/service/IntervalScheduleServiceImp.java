package com.agritechiot.iot.service;

import com.agritechiot.iot.model.IntervalSchedule;
import com.agritechiot.iot.repository.IntervalScheduleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class IntervalScheduleServiceImp implements IntervalScheduleService {
    private final IntervalScheduleRepo intervalScheduleRepo;

    @Override
    public Mono<IntervalSchedule> saveIntervalRecord(IntervalSchedule req) {
        IntervalSchedule intervalSchedule = new IntervalSchedule();
        intervalSchedule.setRunDatetime(req.getRunDatetime());
        intervalSchedule.setDeviceId(req.getDeviceId());
        intervalSchedule.setDuration(req.getDuration());
        intervalSchedule.setReadSensor(req.getReadSensor());
        intervalSchedule.setReadSensor(req.getReadSensor());
        intervalSchedule.setIsRemoved(false);
        return intervalScheduleRepo.save(intervalSchedule);
    }

    @Override
    public Mono<IntervalSchedule> updateIntervalRecord(Integer id, IntervalSchedule req) {
        return intervalScheduleRepo.findById(id)
                .switchIfEmpty(Mono.error(new Exception("INTERVAL_SCHEDULE_LOG_NOT_FOUND")))
                .map(intervalSchedule -> {
                    intervalSchedule.setId(req.getId());
                    intervalSchedule.setInterval(req.getInterval());
                    intervalSchedule.setDeviceId(req.getDeviceId());
                    intervalSchedule.setDuration(req.getDuration());
                    intervalSchedule.setReadSensor(req.getReadSensor());
                    intervalSchedule.setTurnOnWater(req.getTurnOnWater());
                    intervalSchedule.setRunDatetime(req.getRunDatetime());
                    intervalSchedule.setIsRemoved(false);
                    return intervalSchedule;
                }).flatMap(intervalScheduleRepo::save);
    }

    @Override
    public Flux<IntervalSchedule> getListIntervalRecord() {
        return intervalScheduleRepo.findByIsNotDeleted();
    }

    @Override
    public Flux<IntervalSchedule> getListIntervalRecordByDeviceId(String id) {
        return intervalScheduleRepo.findAllByDeviceId(id);
    }

    @Override
    public Mono<IntervalSchedule> getIntervalRecordById(Integer id) {
        return intervalScheduleRepo.findById(id)
                .switchIfEmpty(Mono.error(new Exception("INTERVAL_SCHEDULE_LOG_NOT_FOUND")));
    }

    @Override
    public Mono<Void> softDeleteById(Integer id) {
        return intervalScheduleRepo.findById(id)
                .switchIfEmpty(Mono.error(new Exception("INTERVAL_SCHEDULE_LOG_NOT_FOUND")))
                .map(intervalSchedule -> {
                    intervalSchedule.setId(id);
                    intervalSchedule.setIsRemoved(true);
                    intervalSchedule.setDeletedAt(LocalDateTime.now());
                    return intervalScheduleRepo.save(intervalSchedule);
                }).then();
    }
}
