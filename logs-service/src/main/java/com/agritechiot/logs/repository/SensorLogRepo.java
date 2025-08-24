package com.agritechiot.logs.repository;

import com.agritechiot.logs.model.SensorLog;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface SensorLogRepo extends ReactiveMongoRepository<SensorLog, String> {
    @Query("{ 'data.id': ?0 }")
    Flux<SensorLog> findByDeviceId(String deviceId);

    @Query(value = "{ 'fromTopic': ?0 }", sort = "{ 'dateTime' : -1 }")
    Flux<SensorLog> findByFromTopic(String topic);


    @Query("{ 'data.status': ?0 }")
    Flux<SensorLog> findByStatus(String status);
}
