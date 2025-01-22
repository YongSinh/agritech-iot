package com.agritechiot.agritech_iot.repository;

import com.agritechiot.agritech_iot.model.IntervalSchedule;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IntervalScheduleRepo extends ReactiveCrudRepository<IntervalSchedule, Integer> {
}
