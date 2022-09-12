package com.tiger.service;

import com.tiger.domain.UserDetailsImpl;
import com.tiger.domain.member.Member;
import com.tiger.exception.CustomException;
import com.tiger.exception.StatusCode;
import com.tiger.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Member> member = memberRepository.findByEmailAndIsValid(email, true);
        return member
                .map(UserDetailsImpl::new)
                .orElseThrow(()-> {
                    throw new CustomException(StatusCode.USERNAME_NOT_FOUND);
                });
    }
}
