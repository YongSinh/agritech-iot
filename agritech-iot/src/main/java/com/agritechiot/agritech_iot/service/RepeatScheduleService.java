package com.agritechiot.agritech_iot.service;

import com.agritechiot.agritech_iot.dto.request.RepeatScheduleReq;
import com.agritechiot.agritech_iot.model.RepeatSchedule;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RepeatScheduleService {
    Mono<RepeatSchedule> saveRepeatSchedule(RepeatScheduleReq req);

    Mono<RepeatSchedule> updateRepeatSchedule(Integer id, RepeatScheduleReq req);

    Flux<RepeatSchedule> getListRepeatSchedule();

    Flux<RepeatSchedule> getListRepeatScheduleByDay(String day);

    Flux<RepeatSchedule> getListRepeatScheduleByDeviceId(String deviceId);
}
