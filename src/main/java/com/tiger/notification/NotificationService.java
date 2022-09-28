package com.tiger.notification;

import com.tiger.domain.member.Member;
import com.tiger.websocket.chat.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final EmitterRepository emitterRepository = new EmitterRepositoryImpl();
    private final NotificationRepository notificationRepository;
    private final ChatMessageRepository messageRepository;


    public SseEmitter subscribe(Long memberId, String lastEventId) {

        String emitterId = makeTimeIncludeId(memberId);


        Long timeout = 60L * 1000L * 60L; // 1시간


        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(timeout));


        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));


        String eventId = makeTimeIncludeId(memberId);


        sendNotification(emitter, eventId, emitterId, "EventStream Created. [memberId=" + memberId + "]");


        if (hasLostData(lastEventId)) {
            sendLostData(lastEventId, memberId, emitterId, emitter);
        }

        return emitter;
    }



    private String makeTimeIncludeId(Long memberId) {
        return memberId + "_" + System.currentTimeMillis();
    }


    private void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(eventId)
                    .data(data));
        } catch (IOException exception) {
            emitterRepository.deleteById(emitterId);
        }
    }


    private boolean hasLostData(String lastEventId) {
        return !lastEventId.isEmpty();
    }


    private void sendLostData(String lastEventId, Long memberId, String emitterId, SseEmitter emitter) {
        Map<String, Object> eventCaches = emitterRepository.findAllEventCacheStartWithByMemberId(String.valueOf(memberId));
        eventCaches.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
    }


    @Async
    public void send(Member receiver) {
        Notification notification = notificationRepository.save(createNotification(receiver));
        String receiverId = String.valueOf(receiver.getId());
        String eventId = receiverId + "_" + System.currentTimeMillis();
        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithByMemberId(receiverId);
        emitters.forEach(
                (key, emitter) -> {
                    emitterRepository.saveEventCache(key, notification);
                    sendNotification(emitter, eventId, key, NotificationDto.create(notification));
                }
        );
    }

    private Notification createNotification(Member receiver) {
        return Notification.builder()
                .receiver(receiver)
                .isRead(false)
                .build();
    }

    @Async
    public void sender(Member sender) {
        Notification notification = notificationRepository.save(createNotificationer(sender));
        String senderId = String.valueOf(sender.getId());
        String eventId = senderId + "_" + System.currentTimeMillis();
        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithByMemberId(senderId);
        emitters.forEach(
                (key, emitter) -> {
                    emitterRepository.saveEventCache(key, notification);
                    sendNotification(emitter, eventId, key, NotificationDto.create(notification));
                }
        );
    }

    private Notification createNotificationer(Member sender) {
        return Notification.builder()
                .receiver(sender)
                .isRead(false)
                .build();
    }
}