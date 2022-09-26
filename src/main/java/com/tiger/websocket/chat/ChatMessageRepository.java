package com.tiger.websocket.chat;

import com.tiger.websocket.chatroom.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findAllByRoomIdOrderByIdAsc(Long roomId);

    @Query("SELECT count(msg) FROM ChatMessage msg WHERE msg.senderId =:memberId AND msg.roomId =:roomId AND msg.isRead = false")
    int countMsg(Long memberId, Long roomId);

    @Modifying
    @Transactional
    @Query("UPDATE ChatMessage msg SET msg.isRead = true WHERE msg.roomId = :roomId AND msg.senderId <> :memberId AND msg.isRead = false ")
    void updateChatMessage(Long roomId, Long memberId);

    ChatRoom findByRoomId(Long roomId);

}
