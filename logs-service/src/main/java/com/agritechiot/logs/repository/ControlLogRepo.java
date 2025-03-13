package com.agritechiot.logs.repository;

import com.agritechiot.logs.model.ControlLog;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ControlLogRepo extends ReactiveMongoRepository<ControlLog, Integer> {
    @Query("{ 'device_id': ?0 }") // Filter by deviceId
    Flux<ControlLog> findByDeviceId(String deviceId);
}
