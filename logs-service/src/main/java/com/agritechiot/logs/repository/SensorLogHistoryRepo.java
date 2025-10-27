package com.agritechiot.logs.repository;

import com.agritechiot.logs.model.SensorLogHistory;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorLogHistoryRepo extends ReactiveMongoRepository<SensorLogHistory, String> {

}
