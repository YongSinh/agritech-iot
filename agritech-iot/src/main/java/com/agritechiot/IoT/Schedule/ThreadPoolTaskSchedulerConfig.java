package com.agritechiot.IoT.Schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Slf4j
@Component
public class ThreadPoolTaskSchedulerConfig {
    @Bean
    public org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler taskScheduler() {
        org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler scheduler = new org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler();
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
