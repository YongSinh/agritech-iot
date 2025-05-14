package com.agritechiot.iot.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateScheduleStatusReq {
    private Integer id;
    private Boolean status;
    private Integer batchSize;
}
