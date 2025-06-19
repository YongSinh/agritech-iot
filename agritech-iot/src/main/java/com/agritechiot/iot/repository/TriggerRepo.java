package com.agritechiot.iot.repository;

import com.agritechiot.iot.model.Trigger;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public interface TriggerRepo extends ReactiveCrudRepository<Trigger, Integer> {

    @Query("SELECT * FROM tbl_trigger t WHERE t.sensor = :sensensor AND t.deviceId = :deviceid AND (t.isRemoved = false or t.isRemoved IS NULL)")
    Mono<Trigger> findByDeviceIdAndSensor(@Param("deviceId") String deviceId, @Param("sensor") String sensor);

    @Query("SELECT * FROM tbl_trigger t WHERE LOWER(t.sensor) = LOWER(:sensor) AND t.deviceId = :deviceId AND (t.isRemoved = false OR t.isRemoved IS NULL)")
    Mono<Trigger> findByDeviceIdAndSensorIgnoreCase(@Param("deviceId") String deviceId, @Param("sensor") String sensor);

    @Query("SELECT * FROM tbl_trigger as t where t.isRemoved = false or t.isRemoved IS NULL ORDER BY t.id DESC ")
    Flux<Trigger> findByIsNotDeleted();

    @Query("SELECT * FROM tbl_trigger as t WHERE t.deviceId = :deviceid AND (t.isRemoved = false or t.isRemoved IS NULL) ")
    Mono<Trigger> findByDeviceId(@Param("deviceId") String deviceId);

    @Query("SELECT * FROM tbl_trigger as t WHERE t.deviceId IN (:deviceid) AND (t.isRemoved = false or t.isRemoved IS NULL)")
    Flux<Trigger> findByDeviceIds(@Param("deviceId") List<String> deviceId);

}
