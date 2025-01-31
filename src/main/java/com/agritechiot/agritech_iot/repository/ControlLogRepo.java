package com.agritechiot.agritech_iot.repository;

import com.agritechiot.agritech_iot.model.ControlLog;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ControlLogRepo extends ReactiveCrudRepository<ControlLog, Integer> {
    @Query("SELECT * FROM control_log LIMIT :limit OFFSET :offset")
    Flux<ControlLog> findAllByPage(int limit, int offset);

    @Query("SELECT * FROM control_log WHERE deviceid = :id ORDER BY datetime DESC")
    Flux<ControlLog> findAllByDeviceId(int id);

    Flux<ControlLog> findByDurationBetween(Integer duration, Integer duration2);

}
