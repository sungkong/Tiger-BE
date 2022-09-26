package com.tiger.websocket.chatDto;

import com.tiger.websocket.chat.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponseDto {

    private Long messageId;
    private Long senderId;
    private String senderEmail;
    private String senderName;
    private String message;
    private LocalDateTime date;
    private Boolean isRead = false;
    private String type;


    public MessageResponseDto(MessageRequestDto messageRequestDto) {

        this.messageId = Long.valueOf(messageRequestDto.getMessage());
        this.senderId = messageRequestDto.getSenderId();
        this.senderName = messageRequestDto.getName();
        this.message = messageRequestDto.getMessage();
        this.isRead = messageRequestDto.getIsRead();
        this.type = messageRequestDto.getType();
    }


    public static MessageResponseDto createOf(ChatMessage message, String email, String name) {

        MessageResponseDto responseDto = new MessageResponseDto();

        responseDto.senderEmail = email;
        responseDto.senderName = name;
        responseDto.messageId = message.getId();
        responseDto.message = message.getMessage();
        responseDto.date = message.getCreatedAt();
        responseDto.type = message.getType();

        return responseDto;
    }


    public static MessageResponseDto createFrom(ChatMessage message) {

        MessageResponseDto responseDto = new MessageResponseDto();

        responseDto.senderId = message.getSenderId();
        responseDto.messageId = message.getId();
        responseDto.message = message.getMessage();
        responseDto.date = message.getCreatedAt();
        responseDto.type = message.getType();
        responseDto.isRead = true;
        responseDto.senderEmail = message.getSenderEmail();
        responseDto.senderName = message.getSenderName();

        return responseDto;
    }
}
