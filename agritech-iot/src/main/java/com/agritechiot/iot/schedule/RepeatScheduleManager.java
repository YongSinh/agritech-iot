package com.agritechiot.iot.schedule;

import com.agritechiot.iot.dto.request.DeviceCommandReq;
import com.agritechiot.iot.exception.AppException;
import com.agritechiot.iot.model.IoTDevice;
import com.agritechiot.iot.model.RepeatSchedule;
import com.agritechiot.iot.repository.IoTDeviceRepo;
import com.agritechiot.iot.repository.RepeatScheduleRepo;
import com.agritechiot.iot.service.ControlLogService;
import com.agritechiot.iot.util.GenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledFuture;

@RequiredArgsConstructor
@Slf4j
@Component
public class RepeatScheduleManager {
    private final RepeatScheduleRepo repeatScheduleRepo;
    private final ThreadPoolTaskSchedulerConfig threadPoolTaskSchedulerConfig;
    private final IoTDeviceRepo ioTDeviceRepo;
    private final ControlLogService controlLogService;
    private final ConcurrentMap<String, ScheduledFuture<?>> scheduledFutures = new ConcurrentHashMap<>();

    public void refreshScheduledTasks(ScheduledTaskRegistrar taskRegistrar) {
        if (taskRegistrar == null) {
            log.warn("TaskRegistrar not initialized yet");
            return;
        }

        log.info("🧹 Cancelling all existing tasks...");
        cancelAllScheduledTasks();

        log.info("🔁 Re-registering tasks...");
        // Re-schedule default task

        repeatScheduleRepo.findByIsNotDeleted()
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

    }


    public void refreshScheduledTasksById(Integer id, ScheduledTaskRegistrar taskRegistrar) {
        if (taskRegistrar == null) {
            log.warn("TaskRegistrar not initialized yet: {}", id);
            return;
        }

        log.info("🧹 Cancelling tasks for device {}...", id);
        cancelDeviceTasks(id);

        log.info("🔁 Re-registering tasks for device {}...", id);
        repeatScheduleRepo.findById(id)
                .flatMap(schedule -> {
                    if (Boolean.FALSE.equals(schedule.getStatus())) {
                        cancelDeviceTasks(id);
                        return Mono.empty();  // Skip if we're canceling
                    }
                    return Mono.just(schedule);  // Continue with processing
                })
                // .doOnNext(this::scheduleRepeatTask)
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("⚠️ No schedules found for device {}", id);
                    return Mono.empty();
                }))
                .subscribe(
                        this::scheduleRepeatTask,
                        error -> log.error("Failed to schedule tasks for device {}", id, error),
                        () -> log.info("Completed scheduling tasks for device {}", id)
                );
    }


    private void scheduleRepeatTask(RepeatSchedule schedule) {
        try {
            String taskKey = getTaskKey(schedule);

            String cronExpression = GenUtil.generateCronEveryNMinutes(schedule.getDuration());
            log.info("✅ Scheduled task for device {} at {} {}", schedule.getDeviceId(), schedule.getDay(), schedule.getTime());
            log.info("Status: {}", schedule.getStatus());

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

            scheduledFutures.put(taskKey, future);

            log.debug("✅ Scheduled task for device {} at {} {}", schedule.getDeviceId(), schedule.getDay(), schedule.getTime());
        } catch (Exception e) {
            log.error("❌ Failed to schedule task for device {}", schedule.getDeviceId(), e);
        }
    }


    private void cancelAllScheduledTasks() {
        scheduledFutures.values().forEach(future -> future.cancel(false));
        scheduledFutures.clear();
    }

    private void cancelDeviceTasks(Integer id) {
        try {
            String taskKey = "schedule|" + id;
            ScheduledFuture<?> future = scheduledFutures.get(taskKey);

            if (future != null) {
                log.debug("Cancelling task with key: {}", taskKey);
                boolean cancelled = future.cancel(false);
                if (cancelled) {
                    scheduledFutures.remove(taskKey);
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

    private String getTaskKey(RepeatSchedule schedule) {
        return "schedule|" + schedule.getId();
    }

    public void executeScheduledActions(RepeatSchedule schedule) {
        log.info("🚀 Executing scheduled actions for device {}", schedule.getDeviceId());

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

        log.info(GenUtil.getFirstSensor(device.getSensors()));

        try {
            controlLogService.scheduledTaskToDevice(req).block();
        } catch (Exception e) {
            log.error("❌ Failed to execute scheduled action for device {}: {}", device.getId(), e.getMessage(), e);
            throw new AppException("Scheduled task execution failed");
        }
    }
}
