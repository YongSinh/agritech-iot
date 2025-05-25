package com.agritechiot.iot.dto.response;

import java.time.LocalTime;

public record DeviceJoinDto(
        String deviceId,
        String name,
        String date,
        LocalTime time,
        Boolean status
) {
}
