package com.agritechiot.iot.service;


import com.agritechiot.iot.constant.GenConstant;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LogServiceImp implements LogService {

    @Override
    public void logInfo(String step, String message) {
        log.info("TRACE_ID: [{}] - {}: {}", MDC.get(GenConstant.CORRELATION_ID), step, message);
    }

    @Override
    public void logInfo(String step) {
        log.info("TRACE_ID: [{}] - {}", MDC.get(GenConstant.CORRELATION_ID), step);
    }

    @Override
    public void logMqtt(String step, String message, String topic) {
        log.info("📥 Received message on topic {}: {} - {}", step, topic, message);
    }
}
