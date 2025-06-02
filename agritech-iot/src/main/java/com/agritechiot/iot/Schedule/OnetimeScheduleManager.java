package com.agritechiot.iot.Schedule;

import com.agritechiot.iot.model.OnetimeSchedule;
import com.agritechiot.iot.repository.OnetimeScheduleRepo;
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

@Component
@RequiredArgsConstructor
@Slf4j
public class OnetimeScheduleManager {
    private final OnetimeScheduleRepo onetimeScheduleRepo;
    private final ThreadPoolTaskSchedulerConfig threadPoolTaskSchedulerConfig;
    private final ConcurrentMap<String, ScheduledFuture<?>> oneTimeFutures = new ConcurrentHashMap<>();

    public void refreshOneTimeScheduledTasks(ScheduledTaskRegistrar taskRegistrar) {
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
        if (taskRegistrar == null) {
            log.warn("TaskRegistrar not initialized yet: {}", id);
            return;
        }
        log.info("🧹 One time Cancelling tasks for device {}...", id);
        cancelDeviceTasks(id);

        log.info("🔁 Re-registering tasks for device {}...", id);
        onetimeScheduleRepo.findById(id)
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
            String taskKey = "oneTime_schedule|" + id;
            ScheduledFuture<?> future = oneTimeFutures.get(taskKey);

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


    private void executeScheduledActions(OnetimeSchedule schedule) throws Exception {
        log.info("🚀 Executing scheduled actions for device {}", schedule.getDeviceId());

    }

    private String getTaskKey(OnetimeSchedule schedule) {
        return "oneTime_schedule|" + schedule.getId();
    }

    private void cancelAllScheduledTasks() {
        oneTimeFutures.values().forEach(future -> future.cancel(false));
        oneTimeFutures.clear();
    }

}
