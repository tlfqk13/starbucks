package com.example.starbucks.controller;

import com.example.starbucks.dto.UserFormDto;
import com.example.starbucks.dto.VerifyCodeFormDto;
import com.example.starbucks.entity.User;
import com.example.starbucks.entity.VerifyCode;
import com.example.starbucks.service.EmailService;
import com.example.starbucks.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@RequestMapping("/users")
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    @GetMapping(value = "/new")
    public String userForm(Model model){
        model.addAttribute("userFormDto",new UserFormDto());
        return "user/userForm";
    }

    @PostMapping(value = "/new")
    public String newMember(UserFormDto userFormDto, BindingResult bindingResult,Model model){

        if(bindingResult.hasErrors()){
            return "user/userForm";
        }

        try{
            User user = User.createUser(userFormDto,passwordEncoder);
            userService.saveUser(user);
            emailService.sendSimpleMessage(user.getEmail());

        }catch (Exception e){
            model.addAttribute("errorMessage",e.getMessage());
            return "user/userForm";
        }
        return "redirect:confirm";
    }

    @GetMapping(value="/confirm")
    public String verifyCode(Model model){
        model.addAttribute("verifyCodeFormDto",new VerifyCodeFormDto());
        return "email/emailForm";
    }

    @PostMapping(value = "/confirm")
    public String verifyCodeAction(Model model, @Valid VerifyCodeFormDto verifyCodeFormDto, BindingResult result){
        if(result.hasErrors()){
            return "email/emailForm";
        }
        String token = verifyCodeFormDto.getToken();
        boolean isValid= userService.validateVerifyCode(token);

        if(!isValid){
            return "email/emailForm";
        }else{
            return "redirect:login";
        }
    }

    @GetMapping(value = "/login")
    public String loginUser(){
        return "/user/userLoginForm";
    }

    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg","아이디 또는 비밀번호를 확인해주세요");
        return "/user/userLoginForm";
    }
}
