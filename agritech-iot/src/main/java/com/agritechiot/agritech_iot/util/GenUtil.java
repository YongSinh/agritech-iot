package com.agritechiot.agritech_iot.util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class GenUtil {
    private GenUtil() {
    }

    public static LocalTime parsedTime(String time){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
        return LocalTime.parse(time, formatter);
    }

}
