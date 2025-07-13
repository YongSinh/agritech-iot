package com.agritechiot.iot.dto.request;

import com.agritechiot.iot.constant.GenConstant;

public record SleepCommand(String deviceId) implements DeviceCommand {

    @Override
    public String getSet() {
        return GenConstant.TYPE_SLEEP;
    }
}
