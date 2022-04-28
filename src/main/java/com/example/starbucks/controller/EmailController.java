package com.example.starbucks.controller;

import com.example.starbucks.dto.UserFormDto;
import com.example.starbucks.service.EmailService;
import com.example.starbucks.service.EmailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/email")
@Controller
@RequiredArgsConstructor
public class EmailController {

    @Autowired
    EmailService emailService;

    @GetMapping("/confirm")
    public String emailForm(Model model){
        model.addAttribute("userFormDto",new UserFormDto());
        return "email/emailForm";
    }

    @PostMapping("/confirm")
    public void emailConfirm(UserFormDto userFormDto)throws Exception{
        System.out.println(userFormDto.getEmail());
        System.out.println(userFormDto.getEmail());
        System.out.println(userFormDto.getEmail());
        emailService.sendSimpleMessage(userFormDto.getEmail());
    }

    @PostMapping("/verifyCode")
    @ResponseBody
    public int verifyCode(String code){

        int result = 0;
        if(EmailServiceImpl.ePw.equals(code)){
            result = 1;
        }
        return result;
    }
}
