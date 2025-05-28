package com.agritechiot.iot.service;

public interface LogService {
    void logInfo(String step, String message);

    void logInfo(String step);
}
