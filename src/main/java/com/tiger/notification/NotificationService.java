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
        //emitter 하나하나 에 고유의 값을 주기 위해
        String emitterId = makeTimeIncludeId(memberId);


        Long timeout = 60L * 1000L * 60L; // 1시간
        // 생성된 emiiterId를 기반으로 emitter를 저장
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(timeout));

        //emitter의 시간이 만료된 후 레포에서 삭제
        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

        // 503 에러를 방지하기 위해 처음 연결 진행 시 더미 데이터를 전달
        String eventId = makeTimeIncludeId(memberId);

        // 수 많은 이벤트 들을 구분하기 위해 이벤트 ID에 시간을 통해 구분을 해줌
        sendNotification(emitter, eventId, emitterId, "EventStream Created. [memberId=" + memberId + "]");

        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
        if (hasLostData(lastEventId)) {
            sendLostData(lastEventId, memberId, emitterId, emitter);
        }

        return emitter;
    }


    // SseEmitter를 구분 -> 구분자로 시간을 사용함 ,
    // 시간을 붙혀주는 이유 -> 브라우저에서 여러개의 구독을 진행 시
    //탭 마다 SssEmitter 구분을 위해 시간을 붙여 구분하기 위해 아래와 같이 진행
    private String makeTimeIncludeId(Long memberId) {
        return memberId + "_" + System.currentTimeMillis();
    }

    // 유효시간이 다 지난다면 503 에러가 발생하기 때문에 더미데이터를 발행
    private void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(eventId)
                    .data(data));
        } catch (IOException exception) {
            emitterRepository.deleteById(emitterId);
        }
    }

    // Last - event - id 가 존재한다는 것은 받지 못한 데이터가 있다는 것이다.
    private boolean hasLostData(String lastEventId) {
        return !lastEventId.isEmpty();
    }

    // 받지못한 데이터가 있다면 last - event - id를 기준으로 그 뒤의 데이터를 추출해 알림을 보내주면 된다.
    private void sendLostData(String lastEventId, Long memberId, String emitterId, SseEmitter emitter) {
        Map<String, Object> eventCaches = emitterRepository.findAllEventCacheStartWithByMemberId(String.valueOf(memberId));
        eventCaches.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
    }

    // =============================================
    /*
        : 실제 다른 사용자가 알림을 보낼 수 있는 기능이 필요
        알림을 구성 후 해당 알림에 대한 이벤트를 발생
        -> 어떤 회원에게 알림을 보낼지에 대해 찾고 알림을
        받을 회원의 emitter들을 모두 찾아 해당 emitter를 Send
     */
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
                .isRead(false) // 현재 읽음상태
                .build();
    }
}