package com.tiger.controller;


import com.tiger.domain.member.Member;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/api/member")
@RestController
@Api(tags = "[멤버 컨트롤러]")
public class MemberController {

    private final Logger LOGGER = LoggerFactory.getLogger(MemberController.class);

    @PostMapping("/login")
    @ApiOperation(value = "로그인", notes = "로그인하기")
    public ResponseEntity<?> login(@RequestBody Member member){
        // return ResponseEntity.ok(Map.of("key","value"));
        return ResponseEntity.ok().body("ㅎㅇ");
    }

    @GetMapping("/login/{memberId}")
    @ApiOperation(value = "로그인", notes = "로그인하기")
    public ResponseEntity<?> login(@PathVariable Long memberId){
        LOGGER.info("info log={}", memberId);
        
        return ResponseEntity.ok().body(memberId);
    }
}
