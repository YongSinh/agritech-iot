package com.agritechiot.IoT.dto.request;

import com.agritechiot.IoT.constant.MessageType;
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
