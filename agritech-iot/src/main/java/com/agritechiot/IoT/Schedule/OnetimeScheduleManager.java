package com.agritechiot.IoT.Schedule;

import com.agritechiot.IoT.model.OnetimeSchedule;
import com.agritechiot.IoT.repository.OnetimeScheduleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

@Component
@RequiredArgsConstructor
public class OnetimeScheduleManager {
    private final OnetimeScheduleRepo onetimeScheduleRepo;
    private final List<ScheduledFuture<?>> oneTimeFutures = new ArrayList<>();
    public void refreshOnetimeTasks(ScheduledTaskRegistrar taskRegistrar) {
        // cancel old
        oneTimeFutures.forEach(f -> f.cancel(false));
        oneTimeFutures.clear();

        List<OnetimeSchedule> schedules = onetimeScheduleRepo.findAll()
                .filter(s -> Boolean.TRUE.equals(s.getReadSensor()) || Boolean.TRUE.equals(s.getTurnOnWater()))
                .collectList()
                .block();

        if (schedules != null) {
            schedules.forEach(schedule -> {
                // calculate delay from now
                // schedule with taskScheduler().schedule(...)
            });
        }
    }
}
