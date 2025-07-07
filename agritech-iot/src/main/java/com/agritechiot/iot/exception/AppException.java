package com.agritechiot.iot.exception;

import com.agritechiot.iot.constant.GenConstant;
import lombok.Getter;
import lombok.ToString;
import org.eclipse.paho.client.mqttv3.MqttException;

@Getter
@ToString
public class AppException extends RuntimeException {
    private final String message;
    private final String code;

    public AppException(String msg) {
        super(msg);
        this.message = msg;
        this.code = GenConstant.ERR_CODE;
    }

    public AppException(String message, String code) {
        this.message = message;
        this.code = code;
    }

}
