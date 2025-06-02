package com.agritechiot.iot.repository;

import com.agritechiot.iot.model.Trigger;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface TriggerRepo extends ReactiveCrudRepository<Trigger, Integer> {
    @Query("SELECT * FROM tbl_trigger WHERE tbl_trigger.sensor = :sensensor AND deviceid LIKE CONCAT('%', :deviceId, '%')")
    Flux<Trigger> findByDeviceIdAndSensor(@Param("deviceId") String deviceId, @Param("sensor") String sensor);

    @Query("SELECT * FROM tbl_trigger as t where t.isRemoved = false or t.isRemoved IS NULL ORDER BY t.id DESC ")
    Flux<Trigger> findByIsNotDeleted();

    @Query("SELECT * FROM tbl_trigger as t WHERE t.deviceId = :deviceid")
    Mono<Trigger> findByDeviceId(@Param("deviceId") String deviceId);

    @Query("SELECT * FROM tbl_trigger as t WHERE t.deviceId = :deviceid")
    Flux<Trigger> findByDeviceIds(@Param("deviceId") String deviceId);

}
