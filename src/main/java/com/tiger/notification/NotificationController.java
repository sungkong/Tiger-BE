package com.tiger.notification;

import com.tiger.websocket.chat.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class NotificationController {

    private final ChatMessageService messageService;
    private final NotificationService notificationService;
    private final NotificationRepository notificationRepository;


    @GetMapping(value = "/subscribe/{id}", produces = "text/event-stream")
    public SseEmitter subscribe(@PathVariable Long id,
                                @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        return notificationService.subscribe(id, lastEventId);
    }
}