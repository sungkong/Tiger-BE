package com.tiger.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiger.config.security.jwt.TokenProvider;
import com.tiger.domain.TokenDto;
import com.tiger.domain.UserDetailsImpl;
import com.tiger.domain.member.Member;
import com.tiger.domain.member.dto.KakaoUserInfoDto;
import com.tiger.repository.MemberRepository;
import com.tiger.utils.GenerateRandomUtil;
import com.tiger.utils.MapUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final TokenProvider tokenProvider;

    private final MemberRepository memberRepository;

    private final GenerateRandomUtil generateRandom;

    private final MapUtil map;


    // 카카오 로그인 관련 정보
    @Value("${kakao.access-token.client-id}") private String CLIENT_ID;
    @Value("${kakao.access-token.client-secret}") private String CLIENT_SECRET;
    @Value("${kakao.access-token.redirect-uri}") private String REDIRECT_URI;

    // 카카오 로그인
    public HashMap<String, Object> kakaoLogin(String authorityCode) throws JsonProcessingException {

        // 엑세스 토큰 요청
        String accessToken = getAccessToken(authorityCode);
        // 카카오 사용자 정보 요청
        KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(accessToken);
        // 필요시 회원가입
        Member member = registerKakaoUserIfNeeded(kakaoUserInfo);
        // 강제 로그인
        TokenDto token = forceLogin(member);

        return map.tokenAndMember(token, member);
    }

    // 엑세스 토큰 요청
    private String getAccessToken(String code) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", CLIENT_ID);
        body.add("redirect_uri", REDIRECT_URI);
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        return jsonNode.get("access_token").asText();

    }

    // 카카오 사용자 정보 요청
    private KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        String responseBody = response.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);


        String id = jsonNode.get("id").asText();
        String nickname = jsonNode.get("properties")
                .get("nickname").asText();
        String profileImage = jsonNode.get("properties")
                .get("profile_image").asText();
        String email = jsonNode.get("kakao_account")
                .get("email").asText();

        return new KakaoUserInfoDto(id, nickname, email, profileImage);
    }

    // 필요시 회원가입
    private Member registerKakaoUserIfNeeded(KakaoUserInfoDto kakaoUserInfo) {

        String kakaoUserId = kakaoUserInfo.getKakaoUserId();
        Member kakaoUser = memberRepository.findByKakaoUserIdAndIsValid(kakaoUserId, true).orElse(null);

        if (kakaoUser == null) {
            Member newMember = getNewMemberByConvertingKakaoUserToMember(kakaoUserInfo);

            newMember = memberRepository.save(newMember);
            return newMember;
        }

        return kakaoUser;
    }

    private Member getNewMemberByConvertingKakaoUserToMember(KakaoUserInfoDto kakaoUserInfo) {

        String tel = generateRandom.tel();

        return Member.builder()
                .email(kakaoUserInfo.getEmail())
                .password(UUID.randomUUID().toString())
                .name(kakaoUserInfo.getNickname())
                .tel(tel)
                .isValid(true)
                .kakaoUserId(kakaoUserInfo.getKakaoUserId())
                .profileImage(kakaoUserInfo.getProfileImage())
                .build();
    }

    // 강제 로그인
    private TokenDto forceLogin(Member kakaoUser) {

        UserDetailsImpl userDetails = new UserDetailsImpl(kakaoUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "");

        return tokenProvider.generateTokenDto(authentication);
    }

}
