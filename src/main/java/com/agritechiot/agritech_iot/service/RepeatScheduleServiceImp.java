package com.agritechiot.agritech_iot.service;

import com.agritechiot.agritech_iot.model.RepeatSchedule;
import com.agritechiot.agritech_iot.repository.RepeatScheduleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class RepeatScheduleServiceImp implements RepeatScheduleService {
    private final RepeatScheduleRepo repeatScheduleRepo;

    @Override
    public Mono<RepeatSchedule> saveRepeatSchedule(RepeatSchedule req) {
        RepeatSchedule repeatSchedule = new RepeatSchedule();
        repeatSchedule.setTime(req.getTime());
        repeatSchedule.setDeviceid(req.getDeviceid());
        repeatSchedule.setDuration(req.getDuration());
        repeatSchedule.setDay(req.getDay());
        repeatSchedule.setRead_sensor(req.getRead_sensor());
        repeatSchedule.setTurnOn_water(req.getTurnOn_water());
        return repeatScheduleRepo.save(repeatSchedule);
    }

    @Override
    public Mono<RepeatSchedule> updateRepeatSchedule(Integer id, RepeatSchedule req) {
        return repeatScheduleRepo.findById(id)
                .switchIfEmpty(Mono.error(new Exception("ONE_TIME_SCHEDULE_NOT_FOUND")))
                .map(repeatSchedule -> {
                    repeatSchedule.setId(id);
                    repeatSchedule.setTime(req.getTime());
                    repeatSchedule.setDeviceid(req.getDeviceid());
                    repeatSchedule.setDuration(req.getDuration());
                    repeatSchedule.setDay(req.getDay());
                    repeatSchedule.setRead_sensor(req.getRead_sensor());
                    repeatSchedule.setTurnOn_water(req.getTurnOn_water());
                    return repeatSchedule;
                }).flatMap(repeatScheduleRepo::save);
    }

    @Override
    public Flux<RepeatSchedule> getListRepeatSchedule() {
        return repeatScheduleRepo.findAll();
    }
}
