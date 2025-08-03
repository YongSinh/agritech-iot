package com.agritechiot.iot.schedule;

import com.agritechiot.iot.model.Trigger;
import com.agritechiot.iot.service.TriggerService;
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
    private final TriggerService triggerService;

    public void refreshScheduledTasksById(Trigger trigger, ScheduledTaskRegistrar taskRegistrar) {
        schedulingUtil.withTaskRegistrar(taskRegistrar, registrar -> {
            log.info("🧹 Cancelling tasks for device {}...", trigger.getDeviceId());
            cancelTriggerTasks(trigger);
            log.info("🔁 Re-registering tasks for device {}...", trigger.getId());
        });

    }

    public void refreshScheduledTasks(ScheduledTaskRegistrar taskRegistrar) {
        schedulingUtil.withTaskRegistrar(taskRegistrar, registrar -> {
            log.info("🧹 Cancelling tasks for device...");
            cancelAllScheduledTasks();
            log.info("🔁 Re-registering tasks for device ...");
        });

    }

    public void scheduleTriggerTask(Trigger trigger, String topic) {
        try {
            String taskKey = getTaskKey(trigger);
            String cronExpression = GenUtil.generateCronEveryNMinutes(trigger.getDuration());
            log.info("🚀 scheduled actions is start {} and {}", topic, cronExpression);
            ScheduledFuture<?> future = threadPoolTaskSchedulerConfig.taskScheduler().schedule(
                    () -> {
                        log.info("⏰ Executing scheduled task for device {} at {}", trigger.getDeviceId(), new Date());
                        try {
                            executeScheduledActions(trigger, topic);
                        } catch (Exception e) {
                            throw new IllegalStateException("Executing scheduled ", e);
                        }
                    },
                    new CronTrigger(cronExpression)
            );

            scheduledFutures.put(taskKey, future);
            log.debug("✅ Scheduled task for device {} at {}", trigger.getDeviceId(), trigger.getAction());
        } catch (Exception e) {
            log.error("❌ Failed to schedule task for device {}", trigger.getDeviceId(), e);
        }
    }


    private void cancelAllScheduledTasks() {
        scheduledFutures.values().forEach(future -> future.cancel(false));
        scheduledFutures.clear();
    }

    private void cancelTriggerTasks(Trigger trigger) {
        Integer id = null;
        try {
            String taskKey = getTaskKey(trigger);
            id = trigger.getId();
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


    private void executeScheduledActions(Trigger trigger, String topic) {
        log.info("🚀 Executing scheduled actions for device {} and {}", trigger.getDeviceId(), topic);

        try {
            // 2. Cancel the scheduled task immediately after
            cancelTriggerTasks(trigger);
        } catch (Exception e) {
            log.error("❌ Error executing scheduled actions", e);
        }
    }


    private String getTaskKey(Trigger trigger) {
        return "trigger|" + trigger.getId();
    }


}
