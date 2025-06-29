package com.agritechiot.iot.service.mqtt;

import com.agritechiot.iot.dto.request.MqttTopicReq;
import com.agritechiot.iot.model.MqttTopic;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MqttTopicService {
    Mono<MqttTopic> save(MqttTopicReq req);

    Mono<MqttTopic> deleteById(Integer id);

    Flux<MqttTopic> findAll();

    Mono<MqttTopic> updateById(Integer id, MqttTopicReq req);
}

