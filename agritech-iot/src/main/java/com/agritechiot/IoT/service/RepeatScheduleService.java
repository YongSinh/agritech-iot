package com.agritechiot.IoT.service;

import com.agritechiot.IoT.dto.request.RepeatScheduleReq;
import com.agritechiot.IoT.model.RepeatSchedule;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RepeatScheduleService {
    Mono<RepeatSchedule> saveRepeatSchedule(RepeatScheduleReq req);

    Mono<RepeatSchedule> updateRepeatSchedule(Integer id, RepeatScheduleReq req);

    Flux<RepeatSchedule> getListRepeatSchedule();

    void startRepeatSchedule(String deviceId);

    Flux<RepeatSchedule> getListRepeatScheduleByDay(String day);

    Flux<RepeatSchedule> getListRepeatScheduleByDeviceId(String deviceId);
}
