package com.tiger.utils;

import com.tiger.domain.member.Member;
import com.tiger.repository.MemberRepository;
import com.tiger.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CheckUtil {


    private final MemberRepository memberRepository;

    private final OrderRepository orderRepository;

    public Member validateMember(long memberId) {

        return memberRepository.findById(memberId).orElse(null);
    }
}
