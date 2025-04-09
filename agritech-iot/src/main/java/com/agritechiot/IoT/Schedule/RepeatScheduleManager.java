package com.agritechiot.IoT.Schedule;

import com.agritechiot.IoT.model.RepeatSchedule;
import com.agritechiot.IoT.repository.RepeatScheduleRepo;
import com.agritechiot.IoT.service.RepeatScheduleService;
import com.agritechiot.IoT.util.GenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

@RequiredArgsConstructor
@Slf4j
@Component
public class RepeatScheduleManager {
   private final RepeatScheduleService repeatScheduleService;
   private final RepeatScheduleRepo repeatScheduleRepo;
   private final List<ScheduledFuture<?>> repeatFutures = new ArrayList<>();

   public void refreshRepeatTasks(ThreadPoolTaskScheduler taskScheduler) {
        // cancel old
        repeatFutures.forEach(f -> f.cancel(false));
        repeatFutures.clear();

        List<RepeatSchedule> schedules = repeatScheduleRepo.findAll()
                .filter(s -> Boolean.TRUE.equals(s.getReadSensor()) || Boolean.TRUE.equals(s.getTurnOnWater()))
                .collectList()
                .block();

        if (schedules != null) {
            schedules.forEach(schedule -> {
                String cronExpression = GenUtil.toCronExpression(schedule.getDay(), schedule.getTime());

                log.info("✅ Scheduled task for device {} at {} {}", schedule.getDeviceId(), schedule.getDay(), schedule.getTime());

//                ScheduledFuture<?> future = taskScheduler.schedule(
//                        () -> log.info("⏰ Executing scheduled task for device {} at {}", schedule.getDeviceId(), new Date()),
//                        new CronTrigger(cronExpression)
//                );

//                repeatFutures.add(future);
            });
        }
    }


}
