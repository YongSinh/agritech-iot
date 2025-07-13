package com.agritechiot.iot.constant;

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
    public static final Integer DEFAULT_SLEEP_DURATION = 600;

    private GenConstant() {

    }
}
