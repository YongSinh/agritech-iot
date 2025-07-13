package com.agritechiot.iot.dto.request;

import com.agritechiot.iot.constant.GenConstant;

public record WorkCommand(
        String deviceId,
        String run,
        String sleep
) implements DeviceCommand {

    @Override
    public String getSet() {
        return GenConstant.TYPE_WORK;
    }
}
