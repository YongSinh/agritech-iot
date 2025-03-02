package com.agritechiot.agritech_iot.repository;

import com.agritechiot.agritech_iot.model.IntervalSchedule;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IntervalScheduleRepo extends ReactiveMongoRepository<IntervalSchedule, String> {
}
