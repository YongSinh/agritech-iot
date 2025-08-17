package com.agritechiot.logs.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TopicRes {
    @Id
    private Integer id;
    private String topic;
    private String topicOut;
    private String createdBy;
}
