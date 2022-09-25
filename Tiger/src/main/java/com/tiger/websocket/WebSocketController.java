package com.tiger.websocket;

import com.tiger.domain.member.Member;
import com.tiger.exception.CustomException;
import com.tiger.exception.StatusCode;
import com.tiger.repository.MemberRepository;
import com.tiger.websocket.chat.ChatMessageService;
import com.tiger.websocket.chatDto.MessageRequestDto;
import com.tiger.websocket.chatDto.MessageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final ChatMessageService messageService;
    private final MemberRepository memberRepository;


    @MessageMapping("/chat/message")
    public void message(@RequestBody MessageRequestDto messageRequestDto) {

        Member member = memberRepository.findByIdAndIsValid(messageRequestDto.getSenderId(), true).orElseThrow(()->{
            throw new CustomException(StatusCode.USER_NOT_FOUND);
        });

        String email = member.getEmail();
        String name = member.getName();

        MessageResponseDto messageResponseDto = messageService.saveMessage(messageRequestDto, email, name);

        messageService.sendMessage(messageRequestDto, email, messageResponseDto);
    }
}
