package com.agritechiot.logs.repository;

import com.agritechiot.logs.model.SensorLog;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface SensorLogRepo extends ReactiveMongoRepository<SensorLog, String> {
    @Query("{deviceId: ?0}")
    Flux<SensorLog>  findByDeviceId(String deviceId);
}
