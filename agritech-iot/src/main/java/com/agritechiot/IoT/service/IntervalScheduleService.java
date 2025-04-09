package com.agritechiot.IoT.service;

import com.agritechiot.IoT.model.IntervalSchedule;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IntervalScheduleService {
    Mono<IntervalSchedule> saveIntervalRecord(IntervalSchedule req);

    Mono<IntervalSchedule> updateIntervalRecord(Integer id, IntervalSchedule req);

    Flux<IntervalSchedule> getListIntervalRecord();

    Flux<IntervalSchedule> getListIntervalRecordByDeviceId(String id);

    Mono<IntervalSchedule> getIntervalRecordById(Integer id);

}
