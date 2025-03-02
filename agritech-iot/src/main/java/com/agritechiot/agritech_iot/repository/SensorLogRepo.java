package com.agritechiot.agritech_iot.repository;

import com.agritechiot.agritech_iot.model.SensorLog;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface SensorLogRepo extends ReactiveCrudRepository<SensorLog, String> {
    @Query("SELECT * FROM sensor_log WHERE deviceid  =:deviceId ORDER BY id DESC")
    Flux<SensorLog> findByDeviceId(String deviceId);
}
