package com.agritechiot.agritech_iot.constant;

import java.util.UUID;

public class GenConstant {
    public static final String SUC_MESSAGE = "Successfully";
    public static final String ERR_MESSAGE = "ERR";
    public static final String ERR_CODE = "ERR-000";
    public static final String SUC_CODE = "SUC-000";
    public static final String CORRELATION_ID = "correlation_id";
    public static final String DEFAULT_CORRELATION_ID = UUID.randomUUID().toString();

    private GenConstant() {

    }
}
