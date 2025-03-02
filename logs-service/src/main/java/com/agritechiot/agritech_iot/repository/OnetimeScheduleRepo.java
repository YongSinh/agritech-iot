package com.agritechiot.agritech_iot.repository;

import com.agritechiot.agritech_iot.model.OnetimeSchedule;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OnetimeScheduleRepo extends ReactiveMongoRepository<OnetimeSchedule, String> {
}
