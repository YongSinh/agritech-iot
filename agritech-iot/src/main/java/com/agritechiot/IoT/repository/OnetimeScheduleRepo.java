package com.agritechiot.IoT.repository;

import com.agritechiot.IoT.model.OnetimeSchedule;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDate;


@Repository
public interface OnetimeScheduleRepo extends ReactiveCrudRepository<OnetimeSchedule, Integer> {
    Flux<OnetimeSchedule> findAllByDurationBetween(Integer duration, Integer duration2);

    @Query("SELECT * FROM tbl_onetime_schedule WHERE deviceId = : deviceId ORDER BY id DESC")
    Flux<OnetimeSchedule> findByDeviceId(@Param("deviceId") String deviceId);

    Flux<OnetimeSchedule> findByDate(LocalDate date);

}
