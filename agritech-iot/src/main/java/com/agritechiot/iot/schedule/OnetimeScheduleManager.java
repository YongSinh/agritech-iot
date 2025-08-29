package com.agritechiot.iot.schedule;

import com.agritechiot.iot.constant.GenConstant;
import com.agritechiot.iot.dto.request.DeviceCommandReq;
import com.agritechiot.iot.exception.AppException;
import com.agritechiot.iot.model.IoTDevice;
import com.agritechiot.iot.model.OnetimeSchedule;
import com.agritechiot.iot.repository.IoTDeviceRepo;
import com.agritechiot.iot.repository.OnetimeScheduleRepo;
import com.agritechiot.iot.service.ControlLogService;
import com.agritechiot.iot.service.LogService;
import com.agritechiot.iot.util.GenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledFuture;

@Component
@RequiredArgsConstructor
@Slf4j
public class OnetimeScheduleManager {
    private final OnetimeScheduleRepo onetimeScheduleRepo;
    private final LogService logService;
    private final ThreadPoolTaskSchedulerConfig threadPoolTaskSchedulerConfig;
    private final SchedulingUtil schedulingUtil;
    private final ControlLogService controlLogService;
    private final IoTDeviceRepo ioTDeviceRepo;
    private final ConcurrentMap<String, ScheduledFuture<?>> oneTimeFutures = new ConcurrentHashMap<>();

    public void refreshOneTimeScheduledTasks(ScheduledTaskRegistrar taskRegistrar) {
        if (taskRegistrar == null) {
            log.warn("TaskRegistrar not initialized yet");
            return;
        }
        schedulingUtil.withTaskRegistrar(taskRegistrar, registrar -> {
            // Re-schedule default task
            log.info("🧹 Cancelling all existing oneTime schedule tasks...");
            cancelAllScheduledTasks();

            log.info("🔁 Re-registering tasks...");
            // Re-schedule default task
            onetimeScheduleRepo.findByIsNotDeleted()
                    .flatMap(schedule -> {
                        if (Boolean.FALSE.equals(schedule.getStatus())) {
                            cancelDeviceTasks(schedule.getId());
                            return Mono.empty();  // Skip if we're canceling
                        }
                        return Mono.just(schedule);  // Continue with processing
                    })
                    .switchIfEmpty(Mono.defer(() -> {
                        log.warn("⚠️ No schedules found to process");
                        return Mono.empty();
                    }))
                    .subscribe(
                            this::scheduleRepeatTask,
                            error -> log.error("Failed to schedule tasks", error),
                            () -> log.info("Completed scheduling all tasks")
                    );
        });


    }


    private void scheduleRepeatTask(OnetimeSchedule schedule) {
        try {
            String taskKey = getTaskKey(schedule);

            String cronExpression = GenUtil.createOneTimeCronExpression(schedule.getDate(), schedule.getTime());

            log.info("✅ One time Scheduled task for device {} at {} {}", schedule.getDeviceId(), schedule.getDate(), schedule.getTime());

            ScheduledFuture<?> future = threadPoolTaskSchedulerConfig.taskScheduler().schedule(
                    () -> {
                        log.info("⏰ Executing scheduled task for device {} at {}", schedule.getDeviceId(), new Date());
                        try {
                            executeScheduledActions(schedule);
                        } catch (Exception e) {
                            throw new IllegalStateException("Executing scheduled ", e);
                        }
                    },
                    new CronTrigger(cronExpression)
            );

            oneTimeFutures.put(taskKey, future);

            log.debug("✅ One time scheduled task for device {} at {} {}", schedule.getDeviceId(), schedule.getDate(), schedule.getTime());
        } catch (Exception e) {
            log.error("❌ Failed to schedule task for device {}", schedule.getDeviceId(), e);
        }
    }

    public void refreshOneTimeScheduledTasksById(Integer id, ScheduledTaskRegistrar taskRegistrar) {
        schedulingUtil.withTaskRegistrar(taskRegistrar, registrar -> {

            logService.scheduleLog(GenConstant.LOG_TYPE_CANCEL, id, GenConstant.ONETIME_SCHEDULE);
            cancelDeviceTasks(id);
            onetimeScheduleRepo.findById(id)
                    .flatMap(schedule -> {
                        if (Boolean.FALSE.equals(schedule.getStatus())) {
                            return Mono.empty();  // Skip if we're canceling
                        }
                        executeScheduledActions(schedule);
                        return Mono.just(schedule);  // Continue with processing
                    })
                    // .doOnNext(this::scheduleRepeatTask)
                    .switchIfEmpty(Mono.defer(() -> {
                        logService.scheduleLog(GenConstant.LOG_TYPE_NONE, id, GenConstant.ONETIME_SCHEDULE);
                        return Mono.empty();
                    }))
                    .subscribe(
                            this::scheduleRepeatTask,
                            error -> log.error("Failed to schedule tasks for device {}", id, error),
                            () -> logService.scheduleLog(GenConstant.LOG_TYPE_DONE, id, GenConstant.ONETIME_SCHEDULE)
                    );
        });

    }

    public void refreshOneTimeScheduledTasksByIds(List<Integer> ids, ScheduledTaskRegistrar taskRegistrar) {
        schedulingUtil.withTaskRegistrar(taskRegistrar, registrar -> {
            for (Integer id : ids) {
                logService.scheduleLog(GenConstant.LOG_TYPE_CANCEL, id, GenConstant.ONETIME_SCHEDULE);
                cancelDeviceTasks(id);

                logService.scheduleLog(GenConstant.LOG_TYPE_REGISTER, id, GenConstant.ONETIME_SCHEDULE);
                onetimeScheduleRepo.findById(id)
                        .flatMap(schedule -> {
                            if (Boolean.FALSE.equals(schedule.getStatus())) {
                                return Mono.empty();  // Skip if we're canceling
                            }
                            executeScheduledActions(schedule);
                            return Mono.just(schedule);
                        })
                        .switchIfEmpty(Mono.defer(() -> {
                            logService.scheduleLog(GenConstant.LOG_TYPE_NONE, id, GenConstant.ONETIME_SCHEDULE);
                            return Mono.empty();
                        }))
                        .subscribe(
                                this::scheduleRepeatTask,
                                error -> log.error("Failed to schedule tasks for device {}", id, error),
                                () -> logService.scheduleLog(GenConstant.LOG_TYPE_DONE, id, GenConstant.ONETIME_SCHEDULE)
                        );
            }
        });
    }


    private void cancelDeviceTasks(Integer id) {
        try {
            String taskKey = "oneTime_schedule|" + id;
            ScheduledFuture<?> future = schedulingUtil.getScheduledFuture(taskKey, oneTimeFutures);
            if (future != null) {
                log.debug("Cancelling task with key: {}", taskKey);
                boolean cancelled = future.cancel(false);
                if (cancelled) {
                    oneTimeFutures.remove(taskKey);
                    log.info("Successfully cancelled task for schedule ID {}", id);
                } else {
                    log.warn("Failed to cancel task for schedule ID {}", id);
                }
            } else {
                log.debug("No scheduled task found for ID {}", id);
            }
        } catch (Exception e) {
            log.error("Error cancelling task for schedule ID {}", id, e);
        }
    }


    private String getTaskKey(OnetimeSchedule schedule) {
        return "oneTime_schedule|" + schedule.getId();
    }

    private void cancelAllScheduledTasks() {
        oneTimeFutures.values().forEach(future -> future.cancel(false));
        oneTimeFutures.clear();
    }

    public void executeScheduledActions(OnetimeSchedule schedule) {
        logService.scheduleLog(GenConstant.LOG_TYPE_EXECUTE, schedule.getId(), GenConstant.ONETIME_SCHEDULE);

        IoTDevice device = ioTDeviceRepo.findById(schedule.getDeviceId()).block();
        if (device == null) {
            log.error("❌ Device not found with ID: {}", schedule.getDeviceId());
            throw new AppException("Device not found with ID: " + schedule.getDeviceId());
        }

        DeviceCommandReq req = new DeviceCommandReq();
        req.setDeviceId(device.getId());
        req.setSensor(GenUtil.getFirstSensor(device.getSensors()));
        req.setType(GenUtil.getWorkType(schedule.getTurnOnWater()));
        req.setDuration(schedule.getDuration().toString());
        req.setValveDuration(true);

        try {
            controlLogService.scheduledTaskToDevice(req).block();
        } catch (Exception e) {
            log.error("❌ Failed to execute scheduled action for device {}: {}", device.getId(), e.getMessage(), e);
            throw new AppException("Scheduled task execution failed");
        }
    }
}
