package com.example.starbucks.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UserFormDto {

    // view의 Form에서 넘어올 데이터들

    @NotBlank(message = "아이디는 필수 입력값입니다")
    private String userId;

    @NotBlank(message = "비밀번호는 필수 입력값입니다")
    @Length(min=8, max=16, message = "비밀번호는 8자이상 16자 이하로 입력해주세요")
    private String password;

    @NotBlank(message = "이름은 필수 입력값입니다")
    private String userName;

    @NotBlank(message = "닉네임은 필수 입력값입니다")
    private String nickName;

    @NotBlank(message = "생년월일은 필수 입력값입니다")
    private String birthDate;

    @NotBlank(message = "주소는 필수 입력값입니다")
    private String address;

    @NotBlank(message = "이메일은 필수 입력값입니다")
    @Email(message = "이메일 형식으로 입력해주세요")
    private String email;

    @NotBlank(message = "전화번호는 필수 입력값입니다")
    private String phoneNumber;
    
    
}
