package com.agritechiot.iot.Schedule;

import com.agritechiot.iot.model.IntervalSchedule;
import com.agritechiot.iot.repository.IntervalScheduleRepo;
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

@Component
@RequiredArgsConstructor
@Slf4j
public class IntervalScheduleManager {
    private final IntervalScheduleRepo intervalScheduleRepo;
    private final ThreadPoolTaskSchedulerConfig threadPoolTaskSchedulerConfig;
    private final ConcurrentMap<String, ScheduledFuture<?>> intervalSchedule = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Boolean> deviceStatusMap = new ConcurrentHashMap<>();

    public void refreshIntervalScheduledTasks(ScheduledTaskRegistrar taskRegistrar) {
        if (taskRegistrar == null) {
            log.warn("TaskRegistrar not initialized yet");
            return;
        }

        log.info("🧹 Cancelling all existing tasks...");
        cancelAllScheduledTasks();

        log.info("🔁 Re-registering tasks...");
        // Re-schedule default task
        log.info("🧹 Cancelling all existing oneTime_schedule tasks...");
        cancelAllScheduledTasks();

        log.info("🔁 Re-registering tasks...");
        // Re-schedule default task
        intervalScheduleRepo.findAll()
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

    private void scheduleRepeatTask(IntervalSchedule schedule) {
        try {
            String taskKey = getTaskKey(schedule);

            String cronExpression = "";

            log.info("✅ Interval Scheduled task for device {} at {} {}", schedule.getDeviceId(), schedule.getInterval(), schedule.getRunDatetime());

            ScheduledFuture<?> future = threadPoolTaskSchedulerConfig.taskScheduler().schedule(
                    () -> {
                        log.info("⏰ Executing scheduled task for device {} at {}", schedule.getDeviceId(), new Date());
                        try {
                            executeIntervalActions(schedule);
                        } catch (Exception e) {
                            throw new IllegalStateException("Executing scheduled ", e);
                        }
                    },
                    new CronTrigger(cronExpression)
            );
            intervalSchedule.put(taskKey, future);

            log.debug("✅ Interval scheduled task for device {} at {} {}", schedule.getDeviceId(), schedule.getInterval(), schedule.getRunDatetime());
        } catch (Exception e) {
            log.error("❌ Failed to schedule task for device {}", schedule.getDeviceId(), e);
        }
    }

    public void refreshIntervalScheduledTasksById(Integer id, ScheduledTaskRegistrar taskRegistrar) {
        if (taskRegistrar == null) {
            log.warn("TaskRegistrar not initialized yet: {}", id);
            return;
        }
        log.info("🧹 Interval Cancelling tasks for device {}...", id);
        cancelDeviceTasks(id);

        log.info("🔁 Re-registering tasks for device {}...", id);
        intervalScheduleRepo.findById(id)
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


    private void cancelDeviceTasks(Integer id) {
        try {
            String taskKey = "interval_schedule|" + id;
            ScheduledFuture<?> future = intervalSchedule.get(taskKey);

            if (future != null) {
                log.debug("Cancelling task with key: {}", taskKey);
                boolean cancelled = future.cancel(false);
                if (cancelled) {
                    intervalSchedule.remove(taskKey);
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

    private void stopDeviceOperations(IntervalSchedule schedule) {
        String deviceId = schedule.getDeviceId();
        if (Boolean.TRUE.equals(deviceStatusMap.getOrDefault(deviceId, false))) {
            log.info("🛑 Stopping operations for device {}", deviceId);
            // Implement stop logic
            deviceStatusMap.put(deviceId, false);
        }
    }

    private void executeIntervalActions(IntervalSchedule schedule) {
        String deviceId = schedule.getDeviceId();
        boolean currentStatus = deviceStatusMap.getOrDefault(deviceId, false);

        if (!currentStatus) {
            // Start device operations
            if (Boolean.TRUE.equals(schedule.getTurnOnWater())) {
                log.info("💧 Turning ON water for device {}", deviceId);
                // Implement water on logic
            }
            if (Boolean.TRUE.equals(schedule.getReadSensor())) {
                log.info("📡 Reading sensor for device {}", deviceId);
                // Implement sensor reading logic
            }
            deviceStatusMap.put(deviceId, true);

            // Schedule automatic stop after duration (if specified)
            if (schedule.getDuration() != null && schedule.getDuration() > 0) {
                threadPoolTaskSchedulerConfig.taskScheduler().schedule(
                        () -> stopDeviceOperations(schedule),
                        new Date(System.currentTimeMillis() + schedule.getDuration() * 60 * 1000L)
                );
            }
        } else {
            // Stop device operations if they're already running
            stopDeviceOperations(schedule);
        }
    }

    private String getTaskKey(IntervalSchedule schedule) {
        return "oneTime_schedule|" + schedule.getId();
    }

    private void cancelAllScheduledTasks() {
        intervalSchedule.values().forEach(future -> future.cancel(false));
        intervalSchedule.clear();
        deviceStatusMap.clear();
    }

}
