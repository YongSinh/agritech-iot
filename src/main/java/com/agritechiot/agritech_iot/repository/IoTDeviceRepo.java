package com.agritechiot.agritech_iot.repository;

import com.agritechiot.agritech_iot.model.IoTDevice;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@EnableReactiveMongoRepositories
public interface IoTDeviceRepo extends ReactiveMongoRepository<IoTDevice,String> {

}
