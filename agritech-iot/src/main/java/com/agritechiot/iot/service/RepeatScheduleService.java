package com.agritechiot.iot.service;

import com.agritechiot.iot.dto.request.RepeatScheduleReq;
import com.agritechiot.iot.dto.response.ActiveScheduleRes;
import com.agritechiot.iot.model.RepeatSchedule;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.eclipse.paho.client.mqttv3.MqttException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface RepeatScheduleService {
    Mono<RepeatSchedule> saveRepeatSchedule(RepeatScheduleReq req);

    Mono<RepeatSchedule> updateRepeatSchedule(Integer id, RepeatScheduleReq req);

    Flux<RepeatSchedule> getListRepeatSchedule();

    void startRepeatSchedule(RepeatSchedule repeatSchedule) throws JsonProcessingException, MqttException;

    Flux<RepeatSchedule> getListRepeatScheduleByDay(String day);

    Flux<RepeatSchedule> getListRepeatScheduleByDeviceId(String deviceId);

    Mono<Void> updateListsStatus(List<Integer> ids, boolean newStatus, int batchSize);

    Mono<Void> updateSingleStatus(Integer id, boolean newStatus);

    Mono<ActiveScheduleRes> getActiveRepeatSchedule();

    Mono<Void> softDeleteById(Integer id);

}
