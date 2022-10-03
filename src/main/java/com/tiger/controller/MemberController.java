package com.tiger.controller;


import com.tiger.domain.CommonResponseDto;
import com.tiger.domain.TokenDto;
import com.tiger.domain.member.Member;
import com.tiger.domain.member.dto.EmailCheckRequestDto;
import com.tiger.domain.member.dto.LoginRequestDto;
import com.tiger.domain.member.dto.RegisterRequestDto;
import com.tiger.exception.StatusCode;
import com.tiger.service.MemberService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;


    @ApiOperation(value = "회원가입")
    @PostMapping("/register")
    public CommonResponseDto<?> register(@RequestBody @Valid RegisterRequestDto registerRequestDto) {

        String name = memberService.register(registerRequestDto);

        return CommonResponseDto.success(StatusCode.USER_CREATED, name);
    }

    @ApiOperation(value = "로그인")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDto loginRequestDto) {

        TokenDto token = memberService.login(loginRequestDto);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token.getAuthorization());
        headers.add("RefreshToken", token.getRefreshToken());

        Member member = memberService.findMemberByEmail(loginRequestDto.getEmail());

        return ResponseEntity.ok().headers(headers)
                .body(CommonResponseDto.success(StatusCode.LOGIN_SUCCESS,
                        Map.of("id", member.getId(),
                                "email", member.getEmail(),
                                "name", member.getName(),
                                "tel", member.getTel(),
                                "profileImage", member.getProfileImage())));
    }

    @ApiOperation(value = "로그아웃")
    @DeleteMapping("/logout")
    public CommonResponseDto<?> logout(HttpServletRequest httpServletRequest) {

        String name = memberService.logout(httpServletRequest);

        return CommonResponseDto.success(StatusCode.LOGOUT_SUCCESS, name);
    }

    @ApiOperation(value = "이메일 검증")
    @PostMapping("/emailCheck")
    public CommonResponseDto<?> emailCheck(@RequestBody EmailCheckRequestDto emailCheckRequestDto) {

        if (emailCheckRequestDto.getEmail().isBlank() || emailCheckRequestDto.getEmail().isEmpty()) {
            return CommonResponseDto.fail(StatusCode.INVALID_EMAIL);
        }

        boolean result = memberService.emailCheck(emailCheckRequestDto);

        if (result) {
            return CommonResponseDto.fail(StatusCode.EMAIL_ALREADY_EXISTS);
        }

        return CommonResponseDto.success(StatusCode.USABLE_EMAIL, "");
    }

    @ApiOperation(value = "토큰 재발급")
    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest httpServletRequest) {

        HashMap<String, Object> tokenAndMember = memberService.reissue(httpServletRequest);

        TokenDto token = (TokenDto) tokenAndMember.get("Token");
        Member member = (Member) tokenAndMember.get("Member");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token.getAuthorization());
        headers.add("RefreshToken", token.getRefreshToken());

        return ResponseEntity.ok().headers(headers)
                .body(CommonResponseDto.success(StatusCode.TOKEN_REISSUED,
                        Map.of("id", member.getId(),
                                "email", member.getEmail(),
                                "name", member.getName(),
                                "tel", member.getTel(),
                                "profileImage", member.getProfileImage())));

    }
}
