package com.example.starbucks.dto;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class VerifyCodeFormDto {

    @NotBlank(message = "인증코드를 입력해주세요 ")
    private String token;

    private String userEmail;

}
