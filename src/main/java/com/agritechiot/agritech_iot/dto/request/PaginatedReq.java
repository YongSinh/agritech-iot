package com.agritechiot.agritech_iot.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaginatedReq {
    private Integer page = 0;
    private  Integer size = 10;
}
