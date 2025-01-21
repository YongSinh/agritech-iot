package com.agritechiot.agritech_iot.repository;

import com.agritechiot.agritech_iot.model.ControlLog;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ControlLogRepo extends ReactiveMongoRepository<ControlLog, String> {
}
