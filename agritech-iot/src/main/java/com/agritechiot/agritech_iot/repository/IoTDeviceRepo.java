package com.agritechiot.agritech_iot.repository;


import com.agritechiot.agritech_iot.model.IoTDevice;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;


@Repository
public interface IoTDeviceRepo extends ReactiveCrudRepository<IoTDevice, String> {
    Flux<IoTDevice> findByName(String name);
}
