package com.agritechiot.iot.service;

import com.agritechiot.iot.constant.GenConstant;
import com.agritechiot.iot.dto.request.ControlLogReq;
import com.agritechiot.iot.dto.request.DeviceCommandReq;
import com.agritechiot.iot.dto.request.DeviceCommandReq2;
import com.agritechiot.iot.exception.AppException;
import com.agritechiot.iot.model.ControlLog;
import com.agritechiot.iot.repository.ControlLogRepo;
import com.agritechiot.iot.schedule.TriggerScheduleManager;
import com.agritechiot.iot.service.mqtt.Publisher;
import com.agritechiot.iot.util.GenUtil;
import com.agritechiot.iot.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class ControlLogServiceImp implements ControlLogService {

    private final ControlLogRepo repo;
    private final LogService logService;
    private final Publisher publisher;
    private final ControlLogRepo controlLogRepo;
    private final IoTDeviceService ioTDeviceService;
    private final TriggerService triggerService;
    private final TriggerScheduleManager triggerScheduleManager;

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
        return repo.save(controlLog);
    }

    @Override
    public Mono<ControlLog> updateControlLog(ControlLogReq req) {
        logService.logInfo("REQ_UPDATE_CONTROL_LOG_REQ", JsonUtil.toJson(req));
        return repo.findById(req.getId()).switchIfEmpty(Mono.error(new Exception(GenConstant.NOT_FOUND))).
                map(controlLog -> {
                    controlLog.setId(req.getId());
                    controlLog.setDeviceId(req.getDeviceId());
                    controlLog.setDateTime(req.getDateTime());
                    controlLog.setSentBy(req.getSentBy());
                    controlLog.setDuration(req.getDuration());
                    controlLog.setStatus(req.getStatus());
                    controlLog.setIsRemoved(false);
                    return controlLog;
                }).flatMap(repo::save);
    }

    @Override
    public Flux<ControlLog> getControlLogs() {
        return repo.findByIsNotDeleted();
    }

    @Override
    public Mono<Void> offAndOnControlLogDeviceId(String deviceId, boolean status) {
        return controlLogRepo.updateStatusByDeviceId(deviceId, status)
                .then();
    }

    @Override
    public Mono<Void> offAndOnControlLog(Integer id, boolean status) {
        log.info("REQ_OFF_ON_CONTROL_LOG : {}", id);
        return repo.findById(id).switchIfEmpty(Mono.error(new AppException(GenConstant.NOT_FOUND))).
                map(controlLog -> {
                    controlLog.setId(id);
                    controlLog.setStatus(status);
                    return controlLog;
                }).flatMap(repo::save).then();
    }


    @Override
    public Mono<Void> sendTaskToDevice(DeviceCommandReq req) {
        return controlLogRepo.findById(req.getControlLogId())
                .switchIfEmpty(Mono.error(new AppException(GenConstant.NOT_FOUND)))
                .flatMap(controlLog ->
                        ioTDeviceService.getDeviceById(controlLog.getDeviceId())
                                .flatMap(device ->
                                        buildAndSendDeviceCommand(req, device.getDeviceId())
                                )
                );
    }

    @Override
    public Mono<Void> scheduledTaskToDevice(DeviceCommandReq req) {
        return ioTDeviceService.getDeviceById(req.getDeviceId())
                .switchIfEmpty(Mono.error(new AppException(GenConstant.NOT_FOUND)))
                .flatMap(device ->
                        buildAndSendDeviceCommand(req, device.getDeviceId())
                );
    }

    /**
     * Shared method to get trigger, build DeviceCommandReq, and send command.
     */
    private Mono<Void> buildAndSendDeviceCommand(DeviceCommandReq req, String deviceId) {
        String sensor = Optional.ofNullable(req.getSensor()).orElse("").trim().toLowerCase();

        return triggerService.getTriggerBySensorAndDeviceId(sensor, deviceId)
                .switchIfEmpty(Mono.error(new AppException("Trigger not found for sensor: " + sensor)))
                .flatMap(trigger -> {
                    // Build and send command
                    DeviceCommandReq commandReq = new DeviceCommandReq();
                    commandReq.setDeviceId(deviceId);
                    commandReq.setType(req.getType());
                    commandReq.setState(req.getState());
                    if (req.getDuration() != null) {
                        commandReq.setDuration(req.getDuration());
                    } else {
                        commandReq.setDuration(trigger.getDuration() != null ? trigger.getDuration().toString() : null);
                    }
                    commandReq.setValue(trigger.getValue() != null ? trigger.getValue().toString() : null);
                    commandReq.setControlLogId(req.getControlLogId());
                    commandReq.setValveDuration(req.getValveDuration());

                    return sendDeviceCommand(commandReq);
                });
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

    @Override
    public Mono<Void> sendDeviceCommandCheck(DeviceCommandReq req) {
        logService.logInfo("SEND_DEVICE_CHECK_COMMAND_REQ", JsonUtil.toJson(req));

        return ioTDeviceService.getDeviceById(req.getDeviceId())
                .flatMap(device -> {
                    DeviceCommandReq2 req2 = new DeviceCommandReq2();
                    req2.setDevice(device.getName());
                    req2.setId(GenUtil.extractNumber(device.getDeviceId()));
                    req2.setStatus(GenUtil.convertDashToUnderscore(req.getStatus()));

                    String payload = JsonUtil.toJson(req2);
                    log.info("Sending payload: {}", payload);

                    return Mono.fromRunnable(() -> {
                        try {
                            publisher.publish(device.getMasterDeviceName(), payload, 1, true);
                        } catch (MqttException e) {
                            throw new AppException("Failed to publish MQTT message: " + e.getMessage());
                        }
                    });
                })
                .then();
    }


    @Override
    public Mono<Void> sendDeviceCommand(DeviceCommandReq req) {
        logService.logInfo("SEND_DEVICE_COMMAND_REQ", JsonUtil.toJson(req));
        return ioTDeviceService.getDeviceById(req.getDeviceId())
                .flatMap(device -> {
                    DeviceCommandReq2 req2 = new DeviceCommandReq2();
                    req2.setDevice(device.getName());
                    req2.setId(GenUtil.extractNumber(device.getDeviceId()));

                    String payload;
                    boolean status;

                    switch (req.getType().toLowerCase()) {
                        case GenConstant.TYPE_SLEEP -> {
                            req2.setSet("sleep_now");
                            payload = JsonUtil.toJson(req2);
                            status = false;
                        }
                        case GenConstant.TYPE_WORK -> {
                            req2.setSet("work_run");
                            req2.setValue(req.getValue()); // Get from original request
                            payload = JsonUtil.toJson(req2);
                            status = true;
                        }
                        case GenConstant.TYPE_WORK_SLEEP -> {
                            req2.setSet("work_sleep");
                            req2.setValue(req.getValue()); // Get from original request
                            payload = JsonUtil.toJson(req2);
                            status = true;
                        }
                        case GenConstant.TYPE_VALVE -> {
                            req2.setSet("valve");
                            req2.setState(req.getState()); // e.g., "on" or "off"
                            if (Boolean.TRUE.equals(req.getValveDuration())) {
                                req2.setDuration(req.getDuration()); // e.g., "5"
                            }
                            payload = JsonUtil.toJson(req2);
                            status = true;
                        }
                        case GenConstant.TYPE_LORA_M_ADDRESS -> {
                            req2.setSet("lora_M_address");
                            payload = JsonUtil.toJson(req2);
                            status = true;
                        }
                        case GenConstant.TYPE_LORA_M_CHANNEL -> {
                            req2.setSet("lora_M_channel");
                            payload = JsonUtil.toJson(req2);
                            status = true;
                        }
                        case GenConstant.TYPE_LORA_S_ADDRESS -> {
                            req2.setSet("lora_S_address");
                            payload = JsonUtil.toJson(req2);
                            status = true;
                        }
                        default -> {
                            return Mono.error(new AppException("Invalid command type: " + req.getType()));
                        }
                    }
                    String topic = device.getMasterDeviceName();

                    log.info("Sending payload: {}", payload);
                    return ioTDeviceService.updateDeviceStats(device.getDeviceId(), status)
                            .then(Mono.defer(() -> {
                                if (req.getControlLogId() != null) {
                                    return offAndOnControlLog(req.getControlLogId(), status);
                                } else {
                                    return Mono.empty(); // Skip if controlLogId is null
                                }
                            }))
                            .then(Mono.fromRunnable(() -> {
                                try {
                                    publisher.publish(topic, payload, 1, true);
                                } catch (MqttException e) {
                                    throw new AppException("Failed to publish MQTT message: " + e.getMessage());
                                }
                            }));
                });
    }

}
