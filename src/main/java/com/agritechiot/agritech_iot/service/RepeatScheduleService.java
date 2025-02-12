package com.agritechiot.agritech_iot.service;

import com.agritechiot.agritech_iot.model.RepeatSchedule;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RepeatScheduleService {
    Mono<RepeatSchedule> saveRepeatSchedule(RepeatSchedule req);

    Mono<RepeatSchedule> updateRepeatSchedule(Integer id, RepeatSchedule req);

    Flux<RepeatSchedule> getListRepeatSchedule();
}
