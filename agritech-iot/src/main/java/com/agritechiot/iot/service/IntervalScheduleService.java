package com.agritechiot.iot.service;

import com.agritechiot.iot.model.IntervalSchedule;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IntervalScheduleService {
    Mono<IntervalSchedule> saveIntervalRecord(IntervalSchedule req);

    Mono<IntervalSchedule> updateIntervalRecord(Integer id, IntervalSchedule req);

    Flux<IntervalSchedule> getListIntervalRecord();

    Flux<IntervalSchedule> getListIntervalRecordByDeviceId(String id);

    Mono<IntervalSchedule> getIntervalRecordById(Integer id);

    Mono<Void> updateSingleStatus(Integer id, boolean newStatus);

    Mono<Void> updateListsStatus(List<Integer> ids, boolean newStatus, int batchSize);

    Mono<Void> softDeleteById(Integer id);
}
