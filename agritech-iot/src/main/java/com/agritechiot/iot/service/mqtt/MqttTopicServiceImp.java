package com.agritechiot.iot.service.mqtt;

import com.agritechiot.iot.constant.GenConstant;
import com.agritechiot.iot.dto.request.MqttTopicReq;
import com.agritechiot.iot.exception.AppException;
import com.agritechiot.iot.model.MqttTopic;
import com.agritechiot.iot.repository.MqttTopicRepo;
import com.agritechiot.iot.service.LogService;
import lombok.RequiredArgsConstructor;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MqttTopicServiceImp implements MqttTopicService {
    private final MqttTopicRepo repo;
    private final LogService logService;
    private final Subscriber subscriber;

    @Override
    public Mono<MqttTopic> save(MqttTopicReq req) {
        MqttTopic mqttTopic = new MqttTopic();
        mqttTopic.setTopic(req.getTopic());
        mqttTopic.setIsRemoved(false);
        mqttTopic.setCreatedBy(req.getCreatedBy());
        return repo.save(mqttTopic)
                .doOnSuccess(topic -> {
                    try {
                        subscriber.updateStateDevice();
                    } catch (MqttException e) {
                        throw new AppException(e.getMessage());
                    }
                });
    }

    @Override
    public Mono<MqttTopic> deleteById(Integer id) {
        return repo.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException(GenConstant.NOT_FOUND)))
                .map(mqttTopic -> {
                            mqttTopic.setId(id);
                            mqttTopic.setDeletedAt(LocalDateTime.now());
                            mqttTopic.setIsRemoved(true);
                            return mqttTopic;
                        }
                ).flatMap(repo::save).publishOn(Schedulers.boundedElastic())
                .doOnSuccess(topic -> {
                    try {
                        subscriber.updateStateDevice();
                    } catch (MqttException e) {
                        throw new AppException(e.getMessage());
                    }
                });
    }

    @Override
    public Flux<MqttTopic> findAll() {
        return repo.findByIsNotDeleted();
    }

    @Override
    public Mono<MqttTopic> updateById(Integer id, MqttTopicReq req) {
        logService.logInfo("UPDATE_TOPIC", req);
        return repo.findById(id)
                .switchIfEmpty(Mono.error(new AppException(GenConstant.NOT_FOUND)))
                .map(mqttTopic -> {
                            mqttTopic.setId(id);
                            mqttTopic.setCreatedBy(req.getCreatedBy());
                            mqttTopic.setTopic(req.getTopic());
                            return mqttTopic;
                        }
                ).flatMap(repo::save)
                .publishOn(Schedulers.boundedElastic())
                .doOnSuccess(topic -> {
                    try {
                        subscriber.updateStateDevice();
                    } catch (MqttException e) {
                        throw new AppException(e.getMessage());
                    }
                });
    }
}
