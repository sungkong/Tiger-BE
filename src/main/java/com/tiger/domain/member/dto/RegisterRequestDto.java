package com.tiger.domain.member.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class RegisterRequestDto {

    @NotBlank(message = "이메일를 입력해주세요.")
    @Email(message = "이메일 형식을 지켜주세요")
    private String email;

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    @NotBlank(message = "비밀번호를 확인해주세요.")
    private String passwordConfirm;
}
