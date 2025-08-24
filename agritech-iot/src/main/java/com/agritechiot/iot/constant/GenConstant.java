package com.agritechiot.iot.constant;

import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GenConstant {
    public static final String SUC_MESSAGE = "Successfully";
    public static final String ERR_MESSAGE = "ERR";
    public static final String ERR_CODE = "ERR-000";
    public static final String SUC_CODE = "SUC-000";
    public static final String CORRELATION_ID = "correlation_id";
    public static final String DEFAULT_CORRELATION_ID = UUID.randomUUID().toString();
    public static final String ONETIME_SCHEDULE_TYPE = "Onetime Schedule";
    public static final String REPEAT_SCHEDULE_TYPE = "Repeat Schedule";
    public static final String SUBSCRIBE_MSG_LOG = "✅ Subscribed to topic: {}";
    public static final String NOT_FOUND = "DATA NOT FOUND";
    public static final String INTERVAL_SCHEDULE_LOG_NOT_FOUND = "INTERVAL SCHEDULE LOG NOT FOUND";

    public static final String STATUS_ON = "on";
    public static final String STATUS_ONLINE = "online";
    public static final String STATUS_OFF = "off";
    public static final String TYPE_SLEEP = "sleep";
    public static final String TYPE_WORK = "work";
    public static final String TYPE_WORK_SLEEP = "work-sleep";
    public static final String TYPE_STATUS_CHECK = "check";
    public static final String TYPE_STATUS_AMBIEN_TEMPERATURE = "ambien-temperature";
    public static final String TYPE_STATUS_WATER_FLOW_RATE = "water-flow-rate";
    public static final String TYPE_STATUS_WATER_FLOW_QUANTITY = "water-flow-quantity";
    public static final String TYPE_STATUS_WATER_TOTAL = "water-total";
    public static final String TYPE_STATUS_SOIL_TEMPERATURE = "soil-temperature";
    public static final String TYPE_STATUS_WORK_RUN = "work-run";
    public static final String TYPE_STATUS_WORK_SLEEP = "work-sleep";
    public static final String TYPE_STATUS_LORA_M_ADDRESS = "lora-M-address";
    public static final String TYPE_STATUS_LORA_M_CHANNEL = "lora-M-channel";
    public static final String TYPE_STATUS_LORA_S_ADDRESS = "lora-S-address";
    public static final String TYPE_VALVE = "valve";
    public static final String TYPE_STATUS_READ = "read";

    public static final Flux<Map<String, String>> TYPE_STATUS_FLUX = Flux.just(
            createStatusMap(TYPE_STATUS_CHECK),
            createStatusMap(TYPE_STATUS_READ),
            createStatusMap(TYPE_VALVE),
            createStatusMap(TYPE_STATUS_AMBIEN_TEMPERATURE),
            createStatusMap(TYPE_STATUS_WATER_FLOW_RATE),
            createStatusMap(TYPE_STATUS_WATER_FLOW_QUANTITY),
            createStatusMap(TYPE_STATUS_WATER_TOTAL),
            createStatusMap(TYPE_STATUS_SOIL_TEMPERATURE),
            createStatusMap(TYPE_STATUS_WORK_RUN),
            createStatusMap(TYPE_STATUS_WORK_SLEEP),
            createStatusMap(TYPE_STATUS_LORA_M_ADDRESS),
            createStatusMap(TYPE_STATUS_LORA_M_CHANNEL),
            createStatusMap(TYPE_STATUS_LORA_S_ADDRESS)
    );

    public static final Integer DEFAULT_SLEEP_DURATION = 600;
    public static final String TYPE_LORA_M_ADDRESS = "lora-m-address";
    public static final String TYPE_LORA_M_CHANNEL = "lora-m-channel";
    public static final String TYPE_LORA_S_ADDRESS = "lora-s-address";

    private GenConstant() {

    }

    private static Map<String, String> createStatusMap(String status) {
        Map<String, String> map = new HashMap<>();
        map.put("status", status);
        return map;
    }
}
