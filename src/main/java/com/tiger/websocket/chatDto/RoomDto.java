package com.tiger.websocket.chatDto;

import java.time.LocalDateTime;

public interface RoomDto {

    Long getRoomId();

    Long getReqId();

    Long getAccId();

    String getAccName();

    String getReqName();

    Boolean getAccOut();

    Boolean getReqOut();

    Boolean getIsRead();

    Boolean getAccFixed();

    Boolean getReqFixed();

    Long getIsBanned();

    LocalDateTime getDate();

    String getMessage();

}
