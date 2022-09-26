package com.tiger.websocket.chatroom;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tiger.domain.Timestamped;
import com.tiger.domain.member.Member;
import lombok.Getter;

import javax.persistence.*;

import static com.tiger.websocket.chatroom.ChatRoomService.UserTypeEnum.Type.ACCEPTOR;
import static com.tiger.websocket.chatroom.ChatRoomService.UserTypeEnum.Type.REQUESTER;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect
@Getter
@Entity
public class ChatRoom extends Timestamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Member requester;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Member acceptor;

    @Column(nullable = false)
    private Boolean reqOut;

    @Column(nullable = false)
    private Boolean accOut;

    @Column(nullable = false)
    private Boolean accFixed;

    @Column(nullable = false)
    private Boolean reqFixed;

    public ChatRoom() {

    }

    public static ChatRoom createOf(Member requester, Member acceptor) {

        ChatRoom room = new ChatRoom();

        room.requester = requester;
        room.acceptor = acceptor;
        room.reqOut = false;
        room.accOut = true;
        room.accFixed = false;
        room.reqFixed = false;
        return room;
    }

    public void reqOut(Boolean isOut) {
        this.reqOut = isOut;
    }

    public void accOut(Boolean isOut) {
        this.accOut = isOut;
    }

    public void fixedRoom(String flag) {

        switch (flag) {
            case ACCEPTOR:
                this.accFixed = true;
                break;
            case REQUESTER:
                this.reqFixed = true;
                break;
            default:
                throw new IllegalArgumentException("ChatRoom: 올바른 인자값을 입력해 주세요.(ACCEPTOR/REQUESTER)");
        }

    }

    public void enter() {
        this.accOut = false;
        this.reqOut = false;
    }
}
