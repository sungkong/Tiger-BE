package com.tiger.websocket.chatDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequestDto {

    private Long roomId;
    private Long senderId;
    private String name;
    private String message;
    private String type;
    private Boolean isRead;
}
