package com.agritechiot.iot.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.function.Consumer;

@Service
@Slf4j
@RequiredArgsConstructor
public class SchedulingUtil {

    public void withTaskRegistrar(ScheduledTaskRegistrar taskRegistrar, Consumer<ScheduledTaskRegistrar> action) {
        if (taskRegistrar == null) {
            log.warn("⚠️ TaskRegistrar not initialized yet");
            return;
        }
        action.accept(taskRegistrar);
    }

    public ScheduledFuture<?> getScheduledFuture(String taskKey, Map<String, ScheduledFuture<?>> futureMap) {
        if (futureMap == null) {
            log.warn("Future map is null");
            return null;
        }

        ScheduledFuture<?> future = futureMap.get(taskKey);
        if (future == null) {
            log.warn("No scheduled future found for taskKey: {}", taskKey);
        }

        return future;
    }


}
