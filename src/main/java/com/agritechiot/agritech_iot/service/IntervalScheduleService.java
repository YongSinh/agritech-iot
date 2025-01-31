package com.agritechiot.agritech_iot.service;

import com.agritechiot.agritech_iot.model.IntervalSchedule;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IntervalScheduleService {
    Mono<IntervalSchedule> saveIntervalRecord(IntervalSchedule req);

    Mono<IntervalSchedule> updateIntervalRecord(Integer id, IntervalSchedule req);

    Flux<IntervalSchedule> getListIntervalRecord();

    Flux<IntervalSchedule> getListIntervalRecordByDeviceId(Integer id);

    Mono<IntervalSchedule> getIntervalRecordById(Integer id);

}
