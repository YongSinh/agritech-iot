package com.agritechiot.agritech_iot.service;

import com.agritechiot.agritech_iot.model.SensorLog;
import com.agritechiot.agritech_iot.repository.SensorLogRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SensorLogServiceImp implements SensorLogService {
    private final SensorLogRepo sensorLogRepo;

    @Override
    public Mono<SensorLog> saveSensorLog(SensorLog req) {
        SensorLog sensorLog = new SensorLog();
        sensorLog.setDeviceid(req.getDeviceid());
        sensorLog.setDatetime(req.getDatetime());
        sensorLog.setHumidity(req.getHumidity());
        sensorLog.setSensorid(req.getSensorid());
        sensorLog.setFlow_rate(req.getFlow_rate());
        sensorLog.setFlow_quantity(req.getFlow_quantity());
        sensorLog.setTotal_water(req.getTotal_water());
        sensorLog.setTemperature(req.getTemperature());
        sensorLog.setSoil_moisture(req.getSoil_moisture());
        return sensorLogRepo.save(sensorLog);
    }

    @Override
    public Mono<SensorLog> updateSensorLog(String id, SensorLog req) {
        return sensorLogRepo.findById(id)
                .switchIfEmpty(Mono.error(new Exception("ONE_TIME_SCHEDULE_NOT_FOUND")))
                .map(sensorLog -> {
                    sensorLog.setId(id);
                    sensorLog.setDeviceid(req.getDeviceid());
                    sensorLog.setDatetime(req.getDatetime());
                    sensorLog.setHumidity(req.getHumidity());
                    sensorLog.setSensorid(req.getSensorid());
                    sensorLog.setFlow_rate(req.getFlow_rate());
                    sensorLog.setFlow_quantity(req.getFlow_quantity());
                    sensorLog.setTotal_water(req.getTotal_water());
                    sensorLog.setTemperature(req.getTemperature());
                    sensorLog.setSoil_moisture(req.getSoil_moisture());
                    return sensorLog;
                }).flatMap(sensorLogRepo::save);
    }

    @Override
    public Flux<SensorLog> getListSensorLog() {
        return sensorLogRepo.findAll();
    }

    @Override
    public Flux<SensorLog> getSensorLogByDeviceId(Integer deviceId) {
        return getSensorLogByDeviceId(deviceId);
    }
}
