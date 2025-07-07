package com.agritechiot.iot.dto.request;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SleepCommand implements DeviceCommand{
    private final String deviceId;

    @Override
    public String getDeviceId() {
        return deviceId;
    }

    @Override
    public String getSet() {
        return "sleep";
    }
}
