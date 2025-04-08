package com.agritechiot.IoT.repository;

import com.agritechiot.IoT.model.IntervalSchedule;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface IntervalScheduleRepo extends ReactiveCrudRepository<IntervalSchedule, Integer> {
    @Query("SELECT * FROM  tbl_interval_schedule WHERE deviceid = :id ORDER BY run_datetime DESC")
    Flux<IntervalSchedule> findAllByDeviceId(String id);
}
