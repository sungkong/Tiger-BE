package com.tiger.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tiger.domain.CommonResponseDto;
import com.tiger.domain.TokenDto;
import com.tiger.domain.member.Member;
import com.tiger.exception.StatusCode;
import com.tiger.service.MemberService;
import com.tiger.service.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class OAuthController {

    private final OAuthService oAuthService;

    // 카카오 로그인
    @GetMapping("/user/kakao/callback")
    public ResponseEntity<?> kakaoLogin(@RequestParam(value = "code") String authorityCode) throws JsonProcessingException {

        HashMap<String, Object> tokenAndMember = oAuthService.kakaoLogin(authorityCode);

        TokenDto token = (TokenDto) tokenAndMember.get("Token");
        Member member = (Member) tokenAndMember.get("Member");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token.getAuthorization());
        headers.add("RefreshToken", token.getRefreshToken());

        return ResponseEntity.ok().headers(headers)
                .body(CommonResponseDto.success(StatusCode.LOGIN_SUCCESS,
                        Map.of("id", member.getId(),
                                "email", member.getEmail(),
                                "name", member.getName(),
                                "tel", member.getTel(),
                                "profileImage", member.getProfileImage())));
    }
}
