package com.agritechiot.agritech_iot.repository;

import com.agritechiot.agritech_iot.model.Trigger;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface TriggerRepo extends ReactiveCrudRepository<Trigger, Integer> {
    @Query("SELECT * FROM tbl_trigger WHERE tbl_trigger.sensor = :sensensor AND deviceid LIKE CONCAT('%', :deviceId, '%')")
    Mono<Trigger> findByDeviceIdAndSensor(@Param("deviceId") String deviceId, @Param("sensor") String sensor);

}
