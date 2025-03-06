package com.agritechiot.logs.repository;

import com.agritechiot.logs.model.SensorLog;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorLogRepo extends ReactiveMongoRepository<SensorLog, String> {
}
