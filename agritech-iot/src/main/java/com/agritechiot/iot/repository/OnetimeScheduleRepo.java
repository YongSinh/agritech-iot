package com.agritechiot.iot.repository;

import com.agritechiot.iot.model.OnetimeSchedule;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface OnetimeScheduleRepo extends ReactiveCrudRepository<OnetimeSchedule, Integer> {
    Flux<OnetimeSchedule> findAllByDurationBetween(Integer duration, Integer duration2);

    @Query("SELECT * FROM tbl_onetime_schedule WHERE deviceId = : deviceId ORDER BY id DESC")
    Flux<OnetimeSchedule> findByDeviceId(@Param("deviceId") String deviceId);

    Flux<OnetimeSchedule> findByDate(LocalDate date);

    @Modifying
    @Query("UPDATE tbl_onetime_schedule SET status = :status WHERE id IN (:ids)")
    Mono<Integer> updateStatusForIds(List<Integer> ids, boolean status);

    @Modifying
    @Query("UPDATE tbl_onetime_schedule SET status = :status WHERE id = :id")
    Mono<Integer> updateStatusById(Integer id, boolean status);

    @Query("SELECT COUNT(*) FROM tbl_onetime_schedule WHERE status = true")
    Mono<Long> countActiveOnetimeStatus();

}
