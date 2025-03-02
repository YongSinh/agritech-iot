package com.agritechiot.agritech_iot.service;

import com.agritechiot.agritech_iot.model.OnetimeSchedule;
import com.agritechiot.agritech_iot.repository.OnetimeScheduleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class OnetimeScheduleServiceImp implements OnetimeScheduleService {
    private final OnetimeScheduleRepo onetimeScheduleRepo;

    @Override
    public Mono<OnetimeSchedule> saveOnetimeSchedule(OnetimeSchedule req) {
        OnetimeSchedule onetimeSchedule = new OnetimeSchedule();
        onetimeSchedule.setDuration(req.getDuration());
        onetimeSchedule.setDeviceId(req.getDeviceId());
        onetimeSchedule.setTime(req.getTime());
        onetimeSchedule.setRead_sensor(req.getRead_sensor());
        onetimeSchedule.setDate(req.getDate());
        onetimeSchedule.setTurnOn_water(req.getTurnOn_water());
        return onetimeScheduleRepo.save(onetimeSchedule);
    }

    @Override
    public Mono<OnetimeSchedule> updateOnetimeSchedule(Integer id, OnetimeSchedule req) {
        return onetimeScheduleRepo.findById(id)
                .switchIfEmpty(Mono.error(new Exception("ONE_TIME_SCHEDULE_NOT_FOUND")))
                .map(onetimeSchedule -> {
                    onetimeSchedule.setId(id);
                    onetimeSchedule.setDuration(req.getDuration());
                    onetimeSchedule.setDeviceId(req.getDeviceId());
                    onetimeSchedule.setTime(req.getTime());
                    onetimeSchedule.setRead_sensor(req.getRead_sensor());
                    onetimeSchedule.setDate(req.getDate());
                    onetimeSchedule.setTurnOn_water(req.getTurnOn_water());
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
    public Flux<OnetimeSchedule> getListOnetimeScheduleByDeviceId(Mono<String> deviceId) {
        return onetimeScheduleRepo.findAllByDeviceId(deviceId);
    }
}
