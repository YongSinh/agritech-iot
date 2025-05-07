package com.agritechiot.iot.repository;

import com.agritechiot.iot.model.RepeatSchedule;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface RepeatScheduleRepo extends ReactiveCrudRepository<RepeatSchedule, Integer> {

    Flux<RepeatSchedule> findByDay(String day);

    Flux<RepeatSchedule> findByDeviceId(String deviceId);
}
