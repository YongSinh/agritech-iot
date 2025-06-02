package com.agritechiot.iot.Schedule;

import com.agritechiot.iot.model.IntervalSchedule;
import com.agritechiot.iot.repository.IntervalScheduleRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
        intervalScheduleRepo.findByIsNotDeleted()
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
            String taskKey = getIntervalTaskKey(schedule);

            // Generate cron expression for the interval
            String cronExpression = generateIntervalCron(
                    schedule.getRunDatetime(),
                    schedule.getInterval()
            );

            ScheduledFuture<?> future = threadPoolTaskSchedulerConfig.taskScheduler()
                    .schedule(
                            () -> executeIntervalActions(schedule),
                            new CronTrigger(cronExpression)
                    );

            intervalSchedule.put(taskKey, future);
            log.info("✅ Scheduled interval task for device {} with cron {}",
                    schedule.getDeviceId(), cronExpression);
        } catch (Exception e) {
            log.error("❌ Failed to schedule interval task", e);
        }
    }

    private String generateIntervalCron(LocalDateTime startTime, int intervalMinutes) {
        int minute = startTime.getMinute();
        int hour = startTime.getHour();

        // For intervals that divide 60 evenly
        if (60 % intervalMinutes == 0) {
            return String.format("%d/%d %d * * *", minute, intervalMinutes, hour);
        }
        // For irregular intervals
        else {
            List<String> minutes = new ArrayList<>();
            for (int m = minute; m < 60; m += intervalMinutes) {
                minutes.add(String.valueOf(m));
            }
            return String.format("%s %d * * *", String.join(",", minutes), hour);
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

    private String getIntervalTaskKey(IntervalSchedule schedule) {
        return "interval_schedule|" + schedule.getId();
    }

    private void cancelAllScheduledTasks() {
        intervalSchedule.values().forEach(future -> future.cancel(false));
        intervalSchedule.clear();
        deviceStatusMap.clear();
    }

}
