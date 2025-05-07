package com.agritechiot.iot.service;

import com.agritechiot.iot.dto.request.RepeatScheduleReq;
import com.agritechiot.iot.model.RepeatSchedule;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RepeatScheduleService {
    Mono<RepeatSchedule> saveRepeatSchedule(RepeatScheduleReq req);

    Mono<RepeatSchedule> updateRepeatSchedule(Integer id, RepeatScheduleReq req);

    Flux<RepeatSchedule> getListRepeatSchedule();

    void startRepeatSchedule(RepeatSchedule repeatSchedule) throws Exception;

    Flux<RepeatSchedule> getListRepeatScheduleByDay(String day);

    Flux<RepeatSchedule> getListRepeatScheduleByDeviceId(String deviceId);
}
