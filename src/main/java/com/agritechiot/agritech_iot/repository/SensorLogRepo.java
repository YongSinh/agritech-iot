package com.agritechiot.agritech_iot.repository;

import com.agritechiot.agritech_iot.model.SensorLog;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorLogRepo extends ReactiveCrudRepository<SensorLog, Integer> {
}
