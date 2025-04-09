package com.agritechiot.IoT.service;


import com.agritechiot.IoT.dto.request.OnetimeScheduleReq;
import com.agritechiot.IoT.model.OnetimeSchedule;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OnetimeScheduleService {
    Mono<OnetimeSchedule> saveOnetimeSchedule(OnetimeScheduleReq req);

    Mono<OnetimeSchedule> updateOnetimeSchedule(Integer id, OnetimeScheduleReq req);

    Flux<OnetimeSchedule> getListOnetimeSchedule();

    Flux<OnetimeSchedule> getListOnetimeScheduleDuration(Integer duration, Integer duration2);

    Flux<OnetimeSchedule> getListOnetimeScheduleByDeviceId(String deviceId);

}
