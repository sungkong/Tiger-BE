package com.tiger.domain.member;

import io.swagger.annotations.ApiParam;
import lombok.Data;

@Data
public class Member {

    @ApiParam(value = "사용자 이름", required = false, example = "홍길동")
    private String name;
    @ApiParam(value = "사용자 이메일", required = false, example = "aaa@naver.com")
    private String email;
}
