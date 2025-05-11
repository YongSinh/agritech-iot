package com.agritechiot.iot.repository;

import com.agritechiot.iot.model.ControlLog;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

@Repository
public interface ControlLogRepo extends ReactiveCrudRepository<ControlLog, Integer> {

    @Query("""
                SELECT * FROM control_log
                WHERE (:deviceId IS NULL OR deviceId = :deviceId)
                  AND (:sentBy IS NULL OR sentby = :sentBy)
                  AND (:status IS NULL OR status = :status)
                  AND (:startDate IS NULL OR CAST(datetime AS DATE) >= :startDate)
                  AND (:endDate IS NULL OR CAST(datetime AS DATE) <= :endDate)
            """)
    Flux<ControlLog> findWithFilters(
            @Param("deviceId") String deviceId,
            @Param("sentBy") String sentBy,
            @Param("status") String status,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    Flux<ControlLog> findByDeviceId(String deviceId);
}
