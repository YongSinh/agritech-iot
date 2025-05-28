package com.agritechiot.iot.Schedule;

import com.agritechiot.iot.model.RepeatSchedule;
import com.agritechiot.iot.repository.RepeatScheduleRepo;
import com.agritechiot.iot.service.RepeatScheduleService;
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
    private final RepeatScheduleService repeatScheduleService;
    private final ThreadPoolTaskSchedulerConfig threadPoolTaskSchedulerConfig;
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

        repeatScheduleRepo.findAll()
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

            String cronExpression = GenUtil.buildWeeklyCronExpression(schedule.getDay(), schedule.getTime());

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


    private void executeScheduledActions(RepeatSchedule schedule) throws Exception {
        log.info("🚀 Executing scheduled actions for device {}", schedule.getDeviceId());
        if (Boolean.TRUE.equals(schedule.getReadSensor()) && Boolean.TRUE.equals(schedule.getTurnOnWater())) {
            repeatScheduleService.startRepeatSchedule(schedule);
        } else {
            log.warn("Schedule for device {} does not require any action", schedule.getDeviceId());
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


}
