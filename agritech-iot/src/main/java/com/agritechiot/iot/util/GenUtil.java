package com.agritechiot.iot.util;

import com.agritechiot.iot.constant.GenConstant;
import com.agritechiot.iot.dto.response.MqttMessageSlaveRes;
import com.agritechiot.iot.exception.AppException;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Slf4j
public class GenUtil {
    private GenUtil() {
    }

    public static String getFirstSensor(String sensor) {
        if (sensor == null || sensor.trim().isEmpty()) {
            return "";
        }
        return sensor.split(",")[0].trim().toLowerCase();
    }


    public static String getWorkType(Boolean type) {
        return Boolean.TRUE.equals(type) ? GenConstant.TYPE_VALVE : GenConstant.TYPE_WORK;
    }


    public static Boolean checkOffAndOn(String status) {
        if (!status.equalsIgnoreCase(GenConstant.STATUS_ON)
                && !status.equalsIgnoreCase(GenConstant.STATUS_OFF)
                && !status.equalsIgnoreCase(GenConstant.STATUS_ONLINE)) {
            throw new AppException("Invalid status: " + status);
        }
        return status.equalsIgnoreCase(GenConstant.STATUS_ON)
                || status.equalsIgnoreCase(GenConstant.STATUS_ONLINE);
    }


    public static void validateFields(MqttMessageSlaveRes req) {
        if (req.getDevice() == null) {
            throw new AppException("Missing required field(s): device_id or value");
        }
    }

    /**
     * Generates a one-time cron expression for a specific date and time
     * Format: second minute hour day month ? year
     */

    public static String createOneTimeCronExpression(String date, LocalTime time) {
        LocalDate localDate = LocalDate.parse(date);
        String cronExpression = String.format("%d %d %d %d %d ?",
                time.getSecond(),
                time.getMinute(),
                time.getHour(),
                localDate.getDayOfMonth(),
                localDate.getMonthValue());
        logCronExpression(cronExpression);
        return cronExpression;
    }

    public static String generateCronEveryNMinutes(int minutes) {
        if (minutes <= 0 || minutes > 59) {
            throw new IllegalArgumentException("Minutes must be between 1 and 59.");
        }
        return String.format("0 */%d * * * *", minutes);
    }

    public static String buildWeeklyCronExpression(String day, LocalTime time) {
        // Capitalize the day
        int dayOfWeek = convertDayToCronValue(day);
        log.info("dayOfWeek: {} ", dayOfWeek);
        String cronExpression = String.format("%d %d %d ? * %d",
                time.getSecond(),
                time.getMinute(),
                time.getHour(),
                dayOfWeek);
        logCronExpression(cronExpression);
        // Build cron expression: second minute hour ? * DAY
        return cronExpression;
    }

    public static String buildMinCronExpression(int minutes) {
        String cronExpression = String.format("0 */%d * * * *", minutes);
        logCronExpression(cronExpression);
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

    private static void logCronExpression(String cronExpression) {
        log.info("CRON_EXPRESSION: {} ", cronExpression);
    }
}
