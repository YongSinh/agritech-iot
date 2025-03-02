package com.agritechiot.agritech_iot.repository;

import com.agritechiot.agritech_iot.model.IoTDevice;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IoTDeviceRepo extends ReactiveMongoRepository<IoTDevice, String> {

}
