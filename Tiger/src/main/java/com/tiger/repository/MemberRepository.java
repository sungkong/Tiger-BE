package com.tiger.repository;

import com.tiger.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.vote.AbstractAclVoter;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmailAndIsValid(String email, boolean isValid);

    Optional<Member> findByIdAndIsValid(Long id, boolean isValid);

    Optional<Member> findByKakaoUserIdAndIsValid(String kakaoUserId, boolean isValid);

    boolean existsByEmail(String email);

    Optional<Long> findIdByEmail(String email);
}
