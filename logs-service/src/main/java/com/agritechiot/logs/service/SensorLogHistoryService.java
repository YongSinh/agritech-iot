package com.agritechiot.logs.service;

import com.agritechiot.logs.model.SensorLogHistory;
import com.agritechiot.logs.repository.SensorLogHistoryRepo;
import com.agritechiot.logs.repository.SensorLogRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableScheduling
public class SensorLogHistoryService {
    private final SensorLogRepo sensorLogRepo;
    private final SensorLogHistoryRepo sensorLogHistoryRepo;

    @Scheduled(cron = "0 0 2 ? * SUN") // Runs every Sunday at 2 AM
    public void schedule() {
        log.info("Scheduled to run sensor log history...");

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneWeekAgo = now.minusWeeks(1);

        sensorLogRepo.findByDateRange(oneWeekAgo, now)
                .flatMap(sensorLog -> {
                    log.info("Found sensor log: {}", sensorLog);
                    SensorLogHistory history = new SensorLogHistory();
                    history.setData(sensorLog.getData());
                    history.setFromTopic(sensorLog.getFromTopic());
                    history.setDateTime(sensorLog.getDateTime());
                    return sensorLogHistoryRepo.save(history)
                            .then(sensorLogRepo.delete(sensorLog));
                })
                .doOnComplete(() -> log.info("✅ Sensor log history backup completed successfully"))
                .doOnError(error -> log.error("❌ Error during sensor log backup", error))
                .subscribe(); // <--- THIS IS REQUIRED
    }
}
