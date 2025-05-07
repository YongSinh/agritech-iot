package com.agritechiot.iot.repository;

import com.agritechiot.iot.model.Trigger;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface TriggerRepo extends ReactiveCrudRepository<Trigger, Integer> {
    @Query("SELECT * FROM tbl_trigger WHERE tbl_trigger.sensor = :sensensor AND deviceid LIKE CONCAT('%', :deviceId, '%')")
    Flux<Trigger> findByDeviceIdAndSensor(@Param("deviceId") String deviceId, @Param("sensor") String sensor);

}
