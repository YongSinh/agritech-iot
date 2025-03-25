package com.agritechiot.agritech_iot.util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class GenUtil {
    private GenUtil() {
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
