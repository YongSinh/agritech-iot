package com.agritechiot.iot.repository;


import com.agritechiot.iot.dto.response.DeviceJoinDto;
import com.agritechiot.iot.model.IoTDevice;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Repository
public interface IoTDeviceRepo extends ReactiveCrudRepository<IoTDevice, String> {

    @Query("SELECT i.deviceId FROM tbl_iotdevice as i")
    Flux<String> findByALlDeviceId();

    Flux<IoTDevice> findByName(String name);

    @Query("""
            SELECT count(*) as 'total' FROM tbl_iotdevice
            """)
    Mono<Long> countAllDevices();

    @Query("""
            SELECT count(*) as 'total' FROM tbl_iotdevice as i
            where i.isDeviceOnline = true
            """)
    Mono<Long> countAllDevicesIsOnline();

    @Query("""
            SELECT i.deviceId as deviceId, i.name as name, o.date as date, o.time as time, o.status
            FROM tbl_iotdevice i
            INNER JOIN tbl_onetime_schedule o ON o.deviceId = i.deviceId
            """)
    Flux<DeviceJoinDto> findJoinedDevices();

    @Query("SELECT i.deviceId FROM tbl_iotdevice i")
    Flux<String> findAllTopicNames();

    @Query("SELECT * FROM tbl_iotdevice as i where i.isRemoved = false or i.isRemoved IS NULL")
    Flux<IoTDevice> findByIsNotDeleted();

    @Query("SELECT * FROM view_device_ids_master_and_non_master v where v.is_master =:num ")
    Flux<IoTDevice> findAllDevicesWithMasterFlag(@Param("num") String num);

}

