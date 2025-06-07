package com.agritechiot.iot.repository;

import com.agritechiot.iot.model.IntervalSchedule;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public interface IntervalScheduleRepo extends ReactiveCrudRepository<IntervalSchedule, Integer> {
    @Query("SELECT * FROM  tbl_interval_schedule WHERE deviceid = :id ORDER BY run_datetime DESC")
    Flux<IntervalSchedule> findAllByDeviceId(String id);

    @Query("SELECT * FROM tbl_interval_schedule as i where i.isRemoved = false or i.isRemoved IS NULL")
    Flux<IntervalSchedule> findByIsNotDeleted();

    @Modifying
    @Query("UPDATE tbl_interval_schedule SET status = :status WHERE id IN (:ids)")
    Mono<Integer> updateStatusForIds(List<Integer> ids, boolean status);

    @Modifying
    @Query("UPDATE tbl_interval_schedule SET status = :status WHERE id = :id")
    Mono<Integer> updateStatusById(Integer id, boolean status);
}
