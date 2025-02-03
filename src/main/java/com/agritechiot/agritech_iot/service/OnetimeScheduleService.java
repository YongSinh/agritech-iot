package com.agritechiot.agritech_iot.service;


import com.agritechiot.agritech_iot.model.OnetimeSchedule;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OnetimeScheduleService {
    Mono<OnetimeSchedule> saveOnetimeSchedule(OnetimeSchedule req);

    Mono<OnetimeSchedule> updateOnetimeSchedule(Integer id, OnetimeSchedule req);

    Flux<OnetimeSchedule> getListOnetimeSchedule();

    Flux<OnetimeSchedule> getListOnetimeScheduleDuration(Integer duration, Integer duration2);

    Flux<OnetimeSchedule> getListOnetimeScheduleByDeviceId(Mono<String> deviceId);

}
