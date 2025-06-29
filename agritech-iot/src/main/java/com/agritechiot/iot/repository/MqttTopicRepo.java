package com.agritechiot.iot.repository;

import com.agritechiot.iot.model.MqttTopic;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface MqttTopicRepo extends ReactiveCrudRepository<MqttTopic, Integer> {
    @Query("SELECT * FROM mqtt_topic as mt where mt.isRemoved =false or mt.isRemoved IS NULL")
    Flux<MqttTopic> findByIsNotDeleted();
}
