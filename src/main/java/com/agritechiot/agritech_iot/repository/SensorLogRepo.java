package com.agritechiot.agritech_iot.repository;

import com.agritechiot.agritech_iot.model.SensorLog;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorLogRepo extends ReactiveMongoRepository<SensorLog, String> {
}
