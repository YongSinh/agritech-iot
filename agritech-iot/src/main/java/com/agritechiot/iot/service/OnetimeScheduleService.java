package com.agritechiot.iot.service;


import com.agritechiot.iot.dto.request.OnetimeScheduleReq;
import com.agritechiot.iot.dto.response.ActiveScheduleRes;
import com.agritechiot.iot.model.OnetimeSchedule;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface OnetimeScheduleService {
    Mono<OnetimeSchedule> saveOnetimeSchedule(OnetimeScheduleReq req);

    Mono<OnetimeSchedule> updateOnetimeSchedule(Integer id, OnetimeScheduleReq req);

    Flux<OnetimeSchedule> getListOnetimeSchedule();

    Flux<OnetimeSchedule> getListOnetimeScheduleDuration(Integer duration, Integer duration2);

    Flux<OnetimeSchedule> getListOnetimeScheduleByDeviceId(String deviceId);

    void startOneTimeSchedule(OnetimeSchedule req) throws Exception;

    Mono<Void> updateListsStatus(List<Integer> ids, boolean newStatus, int batchSize);

    Mono<Void> updateSingleStatus(Integer id, boolean newStatus);

    Mono<ActiveScheduleRes> getActiveOnetimeSchedule();

    Mono<Void> softDeleteById(Integer id);
}
