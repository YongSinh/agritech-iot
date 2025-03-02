package com.agritechiot.agritech_iot.repository;

import com.agritechiot.agritech_iot.model.RepeatSchedule;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepeatScheduleRepo extends ReactiveMongoRepository<RepeatSchedule, String> {
}
