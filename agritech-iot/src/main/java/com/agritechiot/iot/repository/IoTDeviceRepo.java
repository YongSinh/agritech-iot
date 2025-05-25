package com.agritechiot.iot.repository;


import com.agritechiot.iot.dto.response.DeviceJoinDto;
import com.agritechiot.iot.model.IoTDevice;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;


@Repository
public interface IoTDeviceRepo extends ReactiveCrudRepository<IoTDevice, String> {

    @Query("SELECT i.deviceId FROM tbl_iotdevice as i")
    Flux<String> findByALlDeviceId();

    Flux<IoTDevice> findByName(String name);

    @Query("""
            SELECT i.deviceId as deviceId, i.name as name, o.date as date, o.time as time, o.status
            FROM tbl_iotdevice i
            INNER JOIN tbl_onetime_schedule o ON o.deviceId = i.deviceId
            """)
    Flux<DeviceJoinDto> findJoinedDevices();

    @Query("SELECT i.deviceId FROM tbl_iotdevice i")
    Flux<String> findAllTopicNames();
}
