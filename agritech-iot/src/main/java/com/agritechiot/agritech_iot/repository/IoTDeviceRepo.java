package com.agritechiot.agritech_iot.repository;


import com.agritechiot.agritech_iot.model.IoTDevice;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;


@Repository
public interface IoTDeviceRepo extends ReactiveCrudRepository<IoTDevice, String> {

    @Query("SELECT i.deviceId FROM tbl_iotdevice as i")
    Flux<String> findByALlDeviceId();

    Flux<IoTDevice> findByName(String name);
}
