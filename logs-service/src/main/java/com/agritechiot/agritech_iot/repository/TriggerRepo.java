package com.agritechiot.agritech_iot.repository;

import com.agritechiot.agritech_iot.model.Trigger;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface TriggerRepo extends ReactiveMongoRepository<Trigger, String> {
}
