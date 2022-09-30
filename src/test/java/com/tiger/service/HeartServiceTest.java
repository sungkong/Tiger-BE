package com.tiger.service;

import com.tiger.domain.CommonResponseDto;
import com.tiger.domain.member.Member;
import com.tiger.domain.vehicle.heart.Heart;
import com.tiger.repository.HeartRepository;
import com.tiger.repository.MemberRepository;
import com.tiger.utils.CheckUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HeartServiceTest {

    @Autowired
    HeartService heartService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    HeartRepository heartRepository;

    @Test
    @DisplayName("찜 목록 리스트")
    void showHeartList(){

        // given
        Member member = memberRepository.findById(4l).get();
        // when
        List<Heart> allByMemberAndVehicleIsValid = heartRepository.findAllByMemberAndVehicleIsValid(member, true).get();
        // then
        Assertions.assertThat(allByMemberAndVehicleIsValid.size()).isEqualTo(2);
    }

}