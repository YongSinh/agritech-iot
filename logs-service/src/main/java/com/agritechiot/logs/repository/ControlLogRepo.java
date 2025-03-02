package com.agritechiot.logs.repository;

import com.agritechiot.logs.model.ControlLog;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ControlLogRepo extends ReactiveMongoRepository<ControlLog, Integer> {
}
