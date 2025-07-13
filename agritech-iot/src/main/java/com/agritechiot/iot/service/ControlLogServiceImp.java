package com.agritechiot.iot.service;

import com.agritechiot.iot.constant.GenConstant;
import com.agritechiot.iot.dto.request.ControlLogReq;
import com.agritechiot.iot.dto.request.DeviceCommandReq;
import com.agritechiot.iot.dto.request.SleepCommand;
import com.agritechiot.iot.dto.request.WorkCommand;
import com.agritechiot.iot.exception.AppException;
import com.agritechiot.iot.model.ControlLog;
import com.agritechiot.iot.repository.ControlLogRepo;
import com.agritechiot.iot.schedule.TriggerScheduleManager;
import com.agritechiot.iot.service.mqtt.Publisher;
import com.agritechiot.iot.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
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
                }).flatMap(repo::save);
    }

    @Override
    public Flux<ControlLog> getControlLogs() {
        return repo.findByIsNotDeleted();
    }

    @Override
    public Mono<Void> offAndOnControlLogDeviceId(String deviceId, boolean status) {
        return controlLogRepo.updateStatusByDeviceId(deviceId, status)
                .switchIfEmpty(Mono.error(new AppException("CONTROL_LOG_NOT_FOUND")))
                .then();
    }

    @Override
    public Mono<Void> offAndOnControlLog(Integer id, boolean status) {
        log.info("REQ_OFF_ON_CONTROL_LOG : {}", id);
        return repo.findById(id).switchIfEmpty(Mono.error(new AppException("CONTROL_LOG_NOT_FOUND"))).
                map(controlLog -> {
                    controlLog.setId(id);
                    controlLog.setStatus(status);
                    return controlLog;
                }).flatMap(repo::save).then();
    }


    @Override
    public Mono<Void> sendTaskToDevice(Integer id, String sensor) {
        return controlLogRepo.findById(id)
                .switchIfEmpty(Mono.error(new AppException(GenConstant.NOT_FOUND)))
                .flatMap(controlLog ->
                        ioTDeviceService.getDeviceById(controlLog.getDeviceId())
                                .flatMap(device ->
                                        triggerService.getTriggerBySensorAndDeviceId(sensor.trim().toLowerCase(), controlLog.getDeviceId())
                                                .switchIfEmpty(Mono.error(new AppException("Trigger not found for sensor: " + sensor)))
                                                .flatMap(trigger -> {
                                                    // Schedule the trigger task
                                                    triggerScheduleManager.scheduleTriggerTask(trigger, device.getMasterDeviceName());

                                                    // Build the device command
                                                    DeviceCommandReq deviceCommandReq = new DeviceCommandReq();
                                                    deviceCommandReq.setDeviceId(controlLog.getDeviceId());
                                                    deviceCommandReq.setType(GenConstant.TYPE_WORK);
                                                    deviceCommandReq.setRun(trigger.getDuration().toString());
                                                    deviceCommandReq.setControlLogId(id);

                                                    // Send the command to the device
                                                    return sendDeviceCommand(deviceCommandReq);
                                                })
                                )
                );
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
    public Mono<Void> sendDeviceCommand(DeviceCommandReq req) {
        logService.logInfo("SEND_DEVICE_COMMAND_REQ", JsonUtil.toJson(req));
        return ioTDeviceService.getDeviceById(req.getDeviceId())
                .flatMap(device -> {
                    String payload;
                    boolean status;
                    switch (req.getType().toLowerCase()) {
                        case GenConstant.TYPE_WORK -> {
                            WorkCommand workCommand = new WorkCommand(
                                    req.getDeviceId(),
                                    req.getRun(),
                                    device.getSleepDuration()
                            );
                            payload = JsonUtil.toJson(workCommand);
                            status = true;
                        }
                        case GenConstant.TYPE_SLEEP -> {
                            SleepCommand sleepCommand = new SleepCommand(device.getDeviceId());
                            payload = JsonUtil.toJson(sleepCommand);
                            status = false;
                        }
                        default -> {
                            return Mono.error(new AppException("Invalid command type: " + req.getType()));
                        }
                    }
                    String topic = device.getMasterDeviceName(); // or other topic logic

                    log.info(payload);
                    return ioTDeviceService.updateDeviceStats(device.getDeviceId(), status)
                            .then(offAndOnControlLog(req.getControlLogId(), status))
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
