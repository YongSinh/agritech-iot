package com.agritechiot.iot.service;

import com.agritechiot.iot.dto.request.ControlLogReq;
import com.agritechiot.iot.model.ControlLog;
import com.agritechiot.iot.repository.ControlLogRepo;
import com.agritechiot.iot.service.mqtt.Publisher;
import com.agritechiot.iot.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Slf4j
@Service
public class ControlLogServiceImp implements ControlLogService {

    private final ControlLogRepo repo;
    private final LogService logService;
    private final Publisher publisher;
    private final ControlLogRepo controlLogRepo;

    @Override
    public Mono<ControlLog> saveControlLog(ControlLogReq req) {
        logService.logInfo("REQ_SAVE_CONTROL_LOG_REQ", JsonUtil.toJson(req));
        ControlLog controlLog = new ControlLog();
        controlLog.setDeviceId(req.getDeviceId());
        controlLog.setDateTime(req.getDateTime());
        controlLog.setDuration(req.getDuration());
        controlLog.setSentBy(req.getSentBy());
        controlLog.setStatus(req.getStatus());
        controlLog.setIsRemoved(false);
        return repo.save(controlLog)
                .flatMap(savedLog -> {
                    if (Boolean.TRUE.equals(savedLog.getStatus())) {
                        try {
                            logService.logInfo("PUBLISH_MESSAGE_TO_DEVICE", JsonUtil.toJson(savedLog));
                            publisher.publish("test", JsonUtil.objectToJsonString(savedLog), 1, true);
                            return Mono.just(savedLog);
                        } catch (Exception e) {
                            return Mono.error(new IllegalStateException("Failed to publish event", e));
                        }
                    }
                    return Mono.just(savedLog);
                });
    }

    @Override
    public Mono<ControlLog> updateControlLog(ControlLogReq req) {
        logService.logInfo("REQ_UPDATE_CONTROL_LOG_REQ", JsonUtil.toJson(req));
        return repo.findById(req.getId()).switchIfEmpty(Mono.error(new Exception("CONTROL_LOG_NOT_FOUND"))).
                map(controlLog -> {
                    controlLog.setId(req.getId());
                    controlLog.setDeviceId(req.getDeviceId());
                    controlLog.setDateTime(req.getDateTime());
                    controlLog.setSentBy(req.getSentBy());
                    controlLog.setDuration(req.getDuration());
                    controlLog.setStatus(req.getStatus());
                    controlLog.setIsRemoved(false);
                    return controlLog;
                }).flatMap(repo::save)
                .flatMap(savedControlLog -> {
                    if (Boolean.TRUE.equals(savedControlLog.getStatus())) {
                        try {
                            logService.logInfo("PUBLISH_MESSAGE_TO_DEVICE", JsonUtil.toJson(savedControlLog));
                            publisher.publish("test", JsonUtil.objectToJsonString(savedControlLog), 1, true);
                            return Mono.just(savedControlLog);
                        } catch (Exception e) {
                            return Mono.error(new IllegalStateException("Failed to publish event", e));
                        }
                    }
                    return Mono.just(savedControlLog);
                });
    }

    @Override
    public Mono<ControlLog> deleteControlLog(Integer id) {
        return null;
    }

    @Override
    public Flux<ControlLog> getControlLogs() {
        return repo.findByIsNotDeleted();
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

    @Override
    public Mono<Void> softDeleteById(Integer id) {
        return controlLogRepo.findById(id)
                .switchIfEmpty(Mono.error(new Exception("NOT_FOUND")))
                .flatMap(req -> {
                    req.setIsRemoved(true);
                    req.setDeletedAt(LocalDateTime.now());
                    return controlLogRepo.save(req);
                })
                .then();
    }
}
