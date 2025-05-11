package com.agritechiot.iot.service;

import com.agritechiot.iot.dto.request.ControlLogReq;
import com.agritechiot.iot.model.ControlLog;
import com.agritechiot.iot.repository.ControlLogRepo;
import com.agritechiot.iot.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Slf4j
@Service
public class ControlLogServiceImp implements ControlLogService {

    private final ControlLogRepo repo;

    @Override
    public Mono<ControlLog> saveControlLog(ControlLogReq req) {
        log.info("REQ_SAVE_CONTROL_LOG_REQ : {}", JsonUtil.toJson(req));
        ControlLog controlLog = new ControlLog();
        controlLog.setDeviceId(req.getDeviceId());
        controlLog.setDateTime(req.getDateTime());
        controlLog.setDeviceId(req.getDeviceId());
        controlLog.setDuration(req.getDuration());
        controlLog.setStatus(req.getStatus());
        return repo.save(controlLog);
    }

    @Override
    public Mono<ControlLog> updateControlLog(ControlLogReq req) {
        log.info("REQ_UPDATE_CONTROL_LOG_REQ : {}", JsonUtil.toJson(req));
        return repo.findById(req.getId()).switchIfEmpty(Mono.error(new Exception("CONTROL_LOG_NOT_FOUND"))).
                map(controlLog -> {
                    controlLog.setId(req.getId());
                    controlLog.setDeviceId(req.getDeviceId());
                    controlLog.setDateTime(req.getDateTime());
                    controlLog.setDeviceId(req.getDeviceId());
                    controlLog.setDuration(req.getDuration());
                    controlLog.setStatus(req.getStatus());
                    return controlLog;
                }).flatMap(repo::save);
    }

    @Override
    public Mono<ControlLog> deleteControlLog(Integer id) {
        return null;
    }

    @Override
    public Flux<ControlLog> getControlLogs() {
        return repo.findAll();
    }

    @Override
    public Mono<ControlLog> offAndOnControlLog(ControlLogReq req) {
        log.info("REQ_OFF_ON_CONTROL_LOG : {}", JsonUtil.toJson(req));
        return repo.findById(req.getId()).switchIfEmpty(Mono.error(new Exception("CONTROL_LOG_NOT_FOUND"))).
                map(controlLog -> {
                    controlLog.setId(req.getId());
                    controlLog.setStatus(req.getStatus());
                    return controlLog;
                }).flatMap(repo::save);
    }

    @Override
    public Flux<ControlLog> getControlLogsByDeviceId(String deviceId) {
        return repo.findByDeviceId(deviceId);
    }

    @Override
    public Flux<ControlLog> getControlLogsWithFilters(ControlLogReq req) {
        return repo.findWithFilters(req.getDeviceId(), req.getSentBy(), req.getStatus(), req.getStartDate(), req.getEndDate());
    }
}
