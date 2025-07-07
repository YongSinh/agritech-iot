package com.agritechiot.iot.dto.request;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WorkCommand implements DeviceCommand {

    private final String deviceId;
    private final String run;
    private final String sleep;

    @Override
    public String getDeviceId() {
        return deviceId;
    }

    @Override
    public String getSet() {
        return "work";
    }
}
