package com.agritechiot.agritech_iot.dto.request;

import com.agritechiot.agritech_iot.constant.MessageType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message {
    private MessageType type;
    private String content;
    private String sender;
    private String sessionId;
}
