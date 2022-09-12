package com.tiger.repository;

import com.tiger.domain.RefreshToken;
import com.tiger.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    void deleteByMember(Member member);

    Optional<RefreshToken> findByMember(Member member);

}
