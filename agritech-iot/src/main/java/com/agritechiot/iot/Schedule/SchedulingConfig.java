package com.agritechiot.iot.Schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;


@Configuration
@Slf4j
@RequiredArgsConstructor
public class SchedulingConfig implements SchedulingConfigurer {
    private final ThreadPoolTaskSchedulerConfig threadPoolTaskSchedulerConfig;
    private final RepeatScheduleManager repeatScheduleManager;
    private final OnetimeScheduleManager onetimeScheduleManager;
    private final IntervalScheduleManager intervalScheduleManager;
    private ScheduledTaskRegistrar taskRegistrar;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        this.taskRegistrar = taskRegistrar;
        log.info("Initializing ScheduledTaskRegistrar...");
        taskRegistrar.setTaskScheduler(threadPoolTaskSchedulerConfig.taskScheduler());
        refreshScheduledTasks();
    }

    public void refreshScheduledTasks() {
        if (this.taskRegistrar == null) {
            log.warn("TaskRegistrar not initialized yet");
            return;
        }
        repeatScheduleManager.refreshScheduledTasks(taskRegistrar);
        onetimeScheduleManager.refreshOneTimeScheduledTasks(taskRegistrar);
        intervalScheduleManager.refreshIntervalScheduledTasks(taskRegistrar);
    }

    public void refreshRepeatScheduledTasksById(Integer id) {
        repeatScheduleManager.refreshScheduledTasksById(id, taskRegistrar);
    }

    public void refreshOnetimeScheduledTasksById(Integer id) {
        onetimeScheduleManager.refreshOneTimeScheduledTasksById(id, taskRegistrar);
    }


}
