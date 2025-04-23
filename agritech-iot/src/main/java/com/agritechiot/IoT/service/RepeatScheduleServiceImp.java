package com.agritechiot.IoT.service;

import com.agritechiot.IoT.dto.request.RepeatScheduleReq;
import com.agritechiot.IoT.model.IoTDevice;
import com.agritechiot.IoT.model.RepeatSchedule;
import com.agritechiot.IoT.repository.IoTDeviceRepo;
import com.agritechiot.IoT.repository.RepeatScheduleRepo;
import com.agritechiot.IoT.service.mqtt.Publisher;
import com.agritechiot.IoT.util.GenUtil;
import com.agritechiot.IoT.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;


@RequiredArgsConstructor
@Service
@Slf4j
public class RepeatScheduleServiceImp implements RepeatScheduleService {
    private final RepeatScheduleRepo repeatScheduleRepo;
    private final Publisher publisher;
    private final IoTDeviceRepo ioTDeviceRepo;
    @Override
    public Mono<RepeatSchedule> saveRepeatSchedule(RepeatScheduleReq req) {
        RepeatSchedule repeatSchedule = new RepeatSchedule();
        repeatSchedule.setTime(GenUtil.parsedTime(req.getTime()));
        repeatSchedule.setDeviceId(req.getDeviceId());
        repeatSchedule.setDuration(req.getDuration());
        repeatSchedule.setDay(req.getDay().toUpperCase());
        repeatSchedule.setReadSensor(req.getReadSensor());
        repeatSchedule.setTurnOnWater(req.getTurnOnWater());
        return repeatScheduleRepo.save(repeatSchedule);
    }

    @Override
    public Mono<RepeatSchedule> updateRepeatSchedule(Integer id, RepeatScheduleReq req) {
        return repeatScheduleRepo.findById(id)
                .switchIfEmpty(Mono.error(new Exception("ONE_TIME_SCHEDULE_NOT_FOUND")))
                .map(repeatSchedule -> {
                    repeatSchedule.setId(id);
                    repeatSchedule.setTime(GenUtil.parsedTime(req.getTime()));
                    repeatSchedule.setDeviceId(req.getDeviceId());
                    repeatSchedule.setDuration(req.getDuration());
                    repeatSchedule.setDay(req.getDay().toUpperCase());
                    repeatSchedule.setReadSensor(req.getReadSensor());
                    repeatSchedule.setTurnOnWater(req.getTurnOnWater());
                    return repeatSchedule;
                }).flatMap(repeatScheduleRepo::save);
    }

    @Override
    public Flux<RepeatSchedule> getListRepeatSchedule() {
        return repeatScheduleRepo.findAll();
    }

    @Override
    public void startRepeatSchedule(RepeatSchedule repeatSchedule) throws Exception {
        log.info("Reading sensors for device {} at {}", repeatSchedule.getDeviceId(), LocalDateTime.now());
        log.info(JsonUtil.objectToJsonString(repeatSchedule));
        IoTDevice ioTDevice = ioTDeviceRepo.findById(repeatSchedule.getDeviceId()).block();
        assert ioTDevice != null;
        log.info(ioTDevice.getName());
        publisher.publish(ioTDevice.getName(), JsonUtil.objectToJsonString(repeatSchedule), 1, true);
    }

    @Override
    public Flux<RepeatSchedule> getListRepeatScheduleByDay(String day) {
        return repeatScheduleRepo.findByDay(day);
    }

    @Override
    public Flux<RepeatSchedule> getListRepeatScheduleByDeviceId(String deviceId) {
        return repeatScheduleRepo.findByDeviceId(deviceId);
    }

}
