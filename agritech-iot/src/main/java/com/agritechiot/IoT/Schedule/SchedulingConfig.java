package com.agritechiot.IoT.Schedule;

import com.agritechiot.IoT.model.RepeatSchedule;
import com.agritechiot.IoT.repository.RepeatScheduleRepo;
import com.agritechiot.IoT.service.RepeatScheduleService;
import com.agritechiot.IoT.util.GenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledFuture;


@Configuration
@Slf4j
@RequiredArgsConstructor
public class SchedulingConfig implements SchedulingConfigurer {

    private final RepeatScheduleRepo repeatScheduleRepo;
    private final RepeatScheduleService repeatScheduleService;
    private ScheduledTaskRegistrar taskRegistrar;
    private final RepeatScheduleManager repeatScheduleManager;
    private final List<ScheduledFuture<?>> scheduledFutures = new ArrayList<>();
    private final List<ScheduledFuture<?>> oneTimeFutures = new ArrayList<>();


    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        this.taskRegistrar = taskRegistrar;
        log.info("Initializing ScheduledTaskRegistrar...");
        taskRegistrar.setTaskScheduler(taskScheduler());
        refreshScheduledTasks();
    }

    public void refreshScheduledTasks() {
        if (this.taskRegistrar == null) {
            log.warn("TaskRegistrar not initialized yet");
            return;
        }

        log.info("🧹 Cancelling existing tasks...");
        for (ScheduledFuture<?> future : scheduledFutures) {
            future.cancel(false); // Cancel without interrupting running tasks
        }
        scheduledFutures.clear();

        log.info("🔁 Re-registering tasks...");
        repeatScheduleManager.refreshRepeatTasks(taskScheduler());
        // Re-schedule default task
//        ScheduledFuture<?> defaultTask = taskScheduler().schedule(
//                () -> log.info("⏰ Executing default scheduled task at {}", new Date()),
//                new CronTrigger("*/5 * * * * ?")
//        );
//        scheduledFutures.add(defaultTask);

        List<RepeatSchedule> schedules = repeatScheduleRepo.findAll()
                .filter(schedule -> Boolean.TRUE.equals(schedule.getReadSensor()) || Boolean.TRUE.equals(schedule.getTurnOnWater()))
                .collectList()
                .block(); // <-- BLOCK REACTIVE HERE

        if (schedules != null && !schedules.isEmpty()) {
            schedules.forEach(this::scheduleRepeatTask);
        } else {
            log.warn("⚠️ No schedules found to process");
        }

    }

    private void scheduleRepeatTask(RepeatSchedule schedule) {
        try {
            String cronExpression = GenUtil.toCronExpression(schedule.getDay(), schedule.getTime());

            log.info("✅ Scheduled task for device {} at {} {}", schedule.getDeviceId(), schedule.getDay(), schedule.getTime());
            ScheduledFuture<?> future = taskScheduler().schedule(
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

            scheduledFutures.add(future);

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

    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(10);
        scheduler.setThreadNamePrefix("scheduled-");
        scheduler.setErrorHandler(t -> log.error("SCHEDULER ERROR", t));
        scheduler.setWaitForTasksToCompleteOnShutdown(true); // Uncommented this line to ensure tasks finish before shutdown
        scheduler.setAwaitTerminationSeconds(30);
        scheduler.initialize();

        log.info("TaskScheduler configured ✅");
        return scheduler;
    }
}
