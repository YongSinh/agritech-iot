package com.agritechiot.agritech_iot.service;

import com.agritechiot.agritech_iot.model.IntervalSchedule;
import com.agritechiot.agritech_iot.repository.IntervalScheduleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class IntervalScheduleServiceImp implements IntervalScheduleService {
    private final IntervalScheduleRepo intervalScheduleRepo;

    @Override
    public Mono<IntervalSchedule> saveIntervalRecord(IntervalSchedule req) {
        IntervalSchedule intervalSchedule = new IntervalSchedule();
        intervalSchedule.setRun_datetime(req.getRun_datetime());
        intervalSchedule.setDeviceid(req.getDeviceid());
        intervalSchedule.setDuration(req.getDuration());
        intervalSchedule.setRead_sensor(req.getRead_sensor());
        intervalSchedule.setRead_sensor(req.getRead_sensor());
        return intervalScheduleRepo.save(intervalSchedule);
    }

    @Override
    public Mono<IntervalSchedule> updateIntervalRecord(Integer id, IntervalSchedule req) {

        return intervalScheduleRepo.findById(id)
                .switchIfEmpty(Mono.error(new Exception("INTERVAL_SCHEDULE_LOG_NOT_FOUND")))
                .map(intervalSchedule -> {
                    intervalSchedule.setId(req.getId());
                    intervalSchedule.setInterval(req.getInterval());
                    intervalSchedule.setDeviceid(req.getDeviceid());
                    intervalSchedule.setDuration(req.getDuration());
                    intervalSchedule.setRead_sensor(req.getRead_sensor());
                    intervalSchedule.setTurnOn_water(req.getTurnOn_water());
                    intervalSchedule.setRun_datetime(req.getRun_datetime());
                    return intervalSchedule;
                }).flatMap(intervalScheduleRepo::save);
    }

    @Override
    public Flux<IntervalSchedule> getListIntervalRecord() {
        return intervalScheduleRepo.findAll();
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
}
