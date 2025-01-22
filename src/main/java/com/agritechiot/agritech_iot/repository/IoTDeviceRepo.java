package com.agritechiot.agritech_iot.repository;

import com.agritechiot.agritech_iot.model.IoTDevice;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IoTDeviceRepo extends ReactiveCrudRepository<IoTDevice, Integer> {

}
