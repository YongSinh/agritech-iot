package com.agritechiot.logs.service;


import com.agritechiot.logs.model.SensorLog;
import com.agritechiot.logs.repository.SensorLogRepo;
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
        sensorLog.setDeviceId(req.getDeviceId());
        sensorLog.setDateTime(req.getDateTime());
        sensorLog.setHumidity(req.getHumidity());
        sensorLog.setSensorId(req.getSensorId());
        sensorLog.setFlow_rate(req.getFlow_rate());
        sensorLog.setFlowQuantity(req.getFlowQuantity());
        sensorLog.setTotalWater(req.getTotalWater());
        sensorLog.setTemperature(req.getTemperature());
        sensorLog.setSoilMoisture(req.getSoilMoisture());
        return sensorLogRepo.save(sensorLog);
    }

    @Override
    public Mono<SensorLog> updateSensorLog(String id, SensorLog req) {
        return sensorLogRepo.findById(id)
                .switchIfEmpty(Mono.error(new Exception("ONE_TIME_SCHEDULE_NOT_FOUND")))
                .map(sensorLog -> {
                    sensorLog.setId(id);
                    sensorLog.setDeviceId(req.getDeviceId());
                    sensorLog.setDateTime(req.getDateTime());
                    sensorLog.setHumidity(req.getHumidity());
                    sensorLog.setSensorId(req.getSensorId());
                    sensorLog.setFlow_rate(req.getFlow_rate());
                    sensorLog.setFlowQuantity(req.getFlowQuantity());
                    sensorLog.setTotalWater(req.getTotalWater());
                    sensorLog.setTemperature(req.getTemperature());
                    sensorLog.setSoilMoisture(req.getSoilMoisture());
                    return sensorLog;
                }).flatMap(sensorLogRepo::save);
    }

    @Override
    public Flux<SensorLog> getListSensorLog() {
        return sensorLogRepo.findAll();
    }

    @Override
    public Flux<SensorLog> getSensorLogByDeviceId(String deviceId) {
        return getSensorLogByDeviceId(deviceId);
    }
}
