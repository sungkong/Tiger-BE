package com.tiger.domain.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestDto {
    private String email;
    private String name;
    private String password;
    private String passwordConfirm;
}
