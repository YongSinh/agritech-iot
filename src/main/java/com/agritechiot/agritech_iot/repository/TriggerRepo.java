package com.agritechiot.agritech_iot.repository;

import com.agritechiot.agritech_iot.model.Trigger;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TriggerRepo extends ReactiveCrudRepository<Trigger, Integer> {
}
