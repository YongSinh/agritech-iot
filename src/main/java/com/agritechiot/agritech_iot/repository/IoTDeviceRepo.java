package com.agritechiot.agritech_iot.repository;


import com.agritechiot.agritech_iot.model.IoTDevice;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface IoTDeviceRepo extends ReactiveCrudRepository<IoTDevice, String> {
    Mono<IoTDevice> findByName(String name);
}
