package com.tiger.notification;

import com.tiger.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class NotificationDto {

    private Long id;

    public static NotificationDto create(Notification notification) {
        return new NotificationDto(notification.getId());
    }

    public static NotificationDto createOf(Notification notification, Member member){

        NotificationDto dto = new NotificationDto();

        dto.id = notification.getId();

        return dto;
    }
}