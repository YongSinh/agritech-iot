package com.agritechiot.agritech_iot.repository;

import com.agritechiot.agritech_iot.model.OnetimeSchedule;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface OnetimeScheduleRepo extends ReactiveCrudRepository<OnetimeSchedule, Integer> {
    Flux<OnetimeSchedule> findAllByDurationBetween(Integer duration, Integer duration2);

    @Query("SELECT * FROM tbl_onetime_schedule WHERE deviceid IN (:deviceId) ORDER BY id DESC")
    Flux<OnetimeSchedule> findAllByDeviceId(Mono<String> deviceId);
}
