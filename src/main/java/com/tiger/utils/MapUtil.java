package com.tiger.utils;

import com.tiger.domain.TokenDto;
import com.tiger.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class MapUtil {

    public HashMap<String, Object> tokenAndMember(TokenDto token, Member member) {
        HashMap<String, Object> tokenAndMember = new HashMap<>();
        tokenAndMember.put("Token", token);
        tokenAndMember.put("Member", member);
        return tokenAndMember;
    }




}
