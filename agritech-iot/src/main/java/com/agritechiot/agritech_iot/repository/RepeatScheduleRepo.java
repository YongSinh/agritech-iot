package com.agritechiot.agritech_iot.repository;

import com.agritechiot.agritech_iot.model.RepeatSchedule;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepeatScheduleRepo extends ReactiveCrudRepository<RepeatSchedule, Integer> {
}
