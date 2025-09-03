package com.agritechiot.iot.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class UpdateScheduleStatusReq {
    private Integer id;
    private List<Integer> ids;
    private Boolean status;
    private Integer batchSize;
}
