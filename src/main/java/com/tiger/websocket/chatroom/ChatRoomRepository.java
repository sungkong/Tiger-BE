package com.tiger.websocket.chatroom;

import com.tiger.domain.member.Member;
import com.tiger.websocket.chatDto.RoomDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query(value =
            "SELECT DISTINCT r.room_id AS roomId, r.acc_out AS accOut, r.req_out AS reqOut, r.acc_fixed AS accFixed, r.req_fixed AS reqFixed, " +
                    "u1.id AS accId, u1.name AS accName, u2.id AS reqId, u2.name AS reqName, " +
                    "msg.message AS message, msg.created_at AS date " +
                    "FROM chat_room r " +
                    "INNER JOIN member u1 ON r.acceptor_id = u1.id " +
                    "INNER JOIN member u2 ON r.requester_id = u2.id " +
                    "INNER JOIN chat_message msg ON (msg.room_id, msg.message_id) " +
                    "IN (SELECT room_id, MAX(message_id) FROM chat_message GROUP BY room_id) AND r.room_id = msg.room_id " +
                    "WHERE (r.acceptor_id = :member OR r.requester_id = :member) " +
                    "ORDER BY r.modified_at DESC",
            nativeQuery = true)
    List<RoomDto> findAllWith(@Param("member") Member member);

    @Query("SELECT room FROM ChatRoom room JOIN FETCH room.acceptor JOIN FETCH room.requester WHERE room.id = :roomId")
    Optional<ChatRoom> findByIdFetch(Long roomId);

    // 채팅방 찾아오기
    @Query("SELECT room FROM ChatRoom room " +
            "WHERE (room.requester = :requester AND room.acceptor = :acceptor) OR " +
            "(room.requester = :acceptor AND room.acceptor = :requester)")
    Optional<ChatRoom> findByMember(@Param("requester") Member requester, @Param("acceptor") Member acceptor);

}
