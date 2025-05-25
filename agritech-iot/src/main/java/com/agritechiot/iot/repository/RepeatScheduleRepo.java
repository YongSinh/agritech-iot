package com.agritechiot.iot.repository;

import com.agritechiot.iot.model.RepeatSchedule;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public interface RepeatScheduleRepo extends ReactiveCrudRepository<RepeatSchedule, Integer> {

    Flux<RepeatSchedule> findByDay(String day);

    Flux<RepeatSchedule> findByDeviceId(String deviceId);

    @Modifying
    @Query("UPDATE tbl_repeat_schedule SET status = :status WHERE id IN (:ids)")
    Mono<Integer> updateStatusForIds(List<Integer> ids, boolean status);

    @Modifying
    @Query("UPDATE tbl_repeat_schedule SET status = :status WHERE id = :id")
    Mono<Integer> updateStatusById(Integer id, boolean status);

    @Query("SELECT COUNT(*) FROM tbl_repeat_schedule WHERE status = true")
    Mono<Long> countActiveRepeatStatus();
}
