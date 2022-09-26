package com.tiger.websocket.chat;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tiger.domain.Timestamped;
import com.tiger.domain.member.Member;
import com.tiger.websocket.chatDto.MessageRequestDto;
import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Getter
@Component
@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class ChatMessage extends Timestamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private Long roomId;

    private String senderEmail;

    private Long senderId;

    private String senderName;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private Boolean isRead;


    public static ChatMessage createOf(MessageRequestDto requestDto, String email, String name) {

        ChatMessage message = new ChatMessage();

        message.senderId = requestDto.getSenderId();
        message.roomId = requestDto.getRoomId();
        message.senderEmail = email;
        message.senderName = name;
        message.message = requestDto.getMessage();
        message.isRead = requestDto.getIsRead();
        message.type = requestDto.getType();

        return message;
    }

    public ChatMessage(Long roomId, Long memberId, String message) {
        this.roomId = roomId;
        this.senderId = memberId;
        this.message = message;
    }


    public static ChatMessage createInitOf(Long roomId) {

        ChatMessage message = new ChatMessage();

        message.roomId = roomId;
        message.senderId = roomId;
        message.isRead = true;
        message.type = "STATUS";

        return message;
    }


    public static ChatMessage createOutOf(Long roomId, Member member) {

        ChatMessage message = new ChatMessage();

        message.roomId = roomId;
        message.senderEmail = member.getEmail();
        message.message = member.getName() + "님이 채팅방을 나갔습니다.";
        message.isRead = true;
        message.type = "STATUS";

        return message;
    }

    public ChatMessage() {

    }

    public void update() {
        this.isRead = true;
    }

}
