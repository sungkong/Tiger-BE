package com.tiger.websocket.chatDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.tiger.websocket.chatroom.ChatRoomService.UserTypeEnum.Type.ACCEPTOR;
import static com.tiger.websocket.chatroom.ChatRoomService.UserTypeEnum.Type.REQUESTER;

@Getter
@NoArgsConstructor
public class RoomResponseDto {

    private Long roomId;
    private Long memberId;
    private String email;
    private String name;
    private String message;
    private LocalDateTime date;
    private Boolean isRead;
    private Boolean isBanned;
    private int unreadCnt;
    private String type;


    public static RoomResponseDto createOf(String type, String flag, RoomDto roomDto, int unreadCnt, Boolean isBanned) {

        RoomResponseDto responseDto = new RoomResponseDto();

        responseDto.roomId = roomDto.getRoomId();
        responseDto.name = roomDto.getAccName();
        responseDto.message = roomDto.getMessage();
        responseDto.date = roomDto.getDate();
        responseDto.isRead = roomDto.getIsRead();
        responseDto.isBanned = isBanned;
        responseDto.unreadCnt = unreadCnt;
        responseDto.email = String.valueOf(roomDto.getAccId());
        responseDto.type = type;

        switch (flag) {

            case ACCEPTOR:

                responseDto.memberId = roomDto.getReqId();
                responseDto.name = roomDto.getReqName();
                break;

            case REQUESTER:

                responseDto.memberId = roomDto.getAccId();
                responseDto.name = roomDto.getAccName();
                break;

            default:
                break;
        }

        return responseDto;
    }

}
