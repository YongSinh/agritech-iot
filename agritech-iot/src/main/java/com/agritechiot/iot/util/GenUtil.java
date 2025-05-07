package com.agritechiot.iot.util;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
@Slf4j
public class GenUtil {
    private GenUtil() {
    }

    /**
     * Generates a one-time cron expression for a specific date and time
     * Format: second minute hour day month ? year
     */
    public static String createOneTimeCronExpression(String date, LocalTime time) {
        LocalDate localDate = LocalDate.parse(date);
        String cronExpression =String.format("%d %d %d %d %d ?",
                time.getSecond(),
                time.getMinute(),
                time.getHour(),
                localDate.getDayOfMonth(),
                localDate.getMonthValue());
        log.info("ONE_TIME_CRON_EXPRESSION: {} ",cronExpression);
        return cronExpression;
    }

    public static String buildWeeklyCronExpression(String day, LocalTime time) {
        // Capitalize the day
        int dayOfWeek = convertDayToCronValue(day);
        log.info("dayOfWeek: {} ",dayOfWeek);
        String cronExpression =String.format("%d %d %d ? * %d",
                time.getSecond(),
                time.getMinute(),
                time.getHour(),
                dayOfWeek);

        log.info("Cron_Expression: {} ",cronExpression);

        // Build cron expression: second minute hour ? * DAY
        return cronExpression;
    }

    private static int convertDayToCronValue(String day) {
        return switch (day.toLowerCase()) {
            case "sunday" -> 0;
            case "monday" -> 1;
            case "tuesday" -> 2;
            case "wednesday" -> 3;
            case "thursday" -> 4;
            case "friday" -> 5;
            case "saturday" -> 6;
            default -> throw new IllegalArgumentException("Invalid day: " + day);
        };
    }

    public static LocalTime parsedTime(String time) {
        if (time == null || time.isEmpty()) {
            return LocalTime.now();
        }
        try {
            // Try with milliseconds first
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
            return LocalTime.parse(time, formatter);
        } catch (DateTimeParseException e1) {
            try {
                // Fall back to seconds-only format
                DateTimeFormatter fallbackFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                return LocalTime.parse(time, fallbackFormatter);
            } catch (DateTimeParseException e2) {
                throw new IllegalArgumentException("Invalid time format. Expected HH:mm:ss.SSS or HH:mm:ss");
            }
        }
    }
}
