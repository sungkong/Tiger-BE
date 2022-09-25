package com.tiger.config;

import antlr.Token;
import com.tiger.config.security.jwt.TokenProvider;
import com.tiger.exception.CustomException;
import com.tiger.exception.StatusCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {
    private final TokenProvider tokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        StompCommand command = accessor.getCommand();

        if (command.compareTo(StompCommand.CONNECT) == 0) {
            log.info("STOMP CONNECT");
        } else if (command.compareTo(StompCommand.SUBSCRIBE) == 0) {
            log.info("STOMP SUBSCRIBE");
        } else if (command.compareTo(StompCommand.SEND) == 0) {
            log.info("STOMP SEND");
        } else {
            log.info("STOMP DEFAULT");
        }

        return message;
    }
}