package com.agritechiot.iot.Schedule;

import com.agritechiot.iot.model.RepeatSchedule;
import com.agritechiot.iot.util.GenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledFuture;

@RequiredArgsConstructor
@Slf4j
@Component
public class TriggerScheduleManager {
    private final ThreadPoolTaskSchedulerConfig threadPoolTaskSchedulerConfig;
    private final ConcurrentMap<String, ScheduledFuture<?>> scheduledFutures = new ConcurrentHashMap<>();
    private final SchedulingUtil schedulingUtil;

    public void refreshScheduledTasksById(Integer id, ScheduledTaskRegistrar taskRegistrar) {
        schedulingUtil.withTaskRegistrar(taskRegistrar, registrar -> {
            log.info("🧹 Cancelling tasks for device {}...", id);
            cancelDeviceTasks(id);

            log.info("🔁 Re-registering tasks for device {}...", id);

        });

    }

    private void scheduleRepeatTask(RepeatSchedule schedule) {
        try {
            String taskKey = getTaskKey(schedule);

            String cronExpression = GenUtil.buildWeeklyCronExpression(schedule.getDay(), schedule.getTime());

            log.info("✅ Scheduled task for device {} at {} {}", schedule.getDeviceId(), schedule.getDay(), schedule.getTime());
            log.info("Status: {}", schedule.getStatus());

            ScheduledFuture<?> future = threadPoolTaskSchedulerConfig.taskScheduler().schedule(
                    () -> log.info("⏰ Executing scheduled task for device {} at {}", schedule.getDeviceId(), new Date()),
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


}
