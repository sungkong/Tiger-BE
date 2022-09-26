package com.tiger.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class KakaoUserInfoDto {
    private String kakaoUserId;
    private String nickname;
    private String email;
    private String profileImage;
}
