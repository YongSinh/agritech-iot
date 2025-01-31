package com.agritechiot.agritech_iot.repository;

import com.agritechiot.agritech_iot.model.OnetimeSchedule;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OnetimeScheduleRepo extends ReactiveCrudRepository<OnetimeSchedule, Integer> {
}
