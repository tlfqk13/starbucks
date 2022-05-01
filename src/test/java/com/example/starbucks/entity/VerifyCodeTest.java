package com.example.starbucks.entity;

import com.example.starbucks.dto.UserFormDto;
import com.example.starbucks.repository.UserRepository;
import com.example.starbucks.repository.VerifyCodeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class VerifyCodeTest {

    @Autowired
    VerifyCodeRepository verifyCodeRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @PersistenceContext
    EntityManager em;

    public User createUser(){

        UserFormDto userFormDto = new UserFormDto();
        userFormDto.setUserId("test");
        userFormDto.setPassword("testPassword");
        userFormDto.setUserName("손동규");
        userFormDto.setNickName("랄라나");
        userFormDto.setBirthDate("940719");
        userFormDto.setAddress("경기도 안양시");
        userFormDto.setEmail("sondong@naver.com");
        userFormDto.setPhoneNumber("010-1234-5678");

        return User.createUser(userFormDto,passwordEncoder);
    }

    @Test
    @DisplayName("인증코드 회원 엔티티 매핑 조회 테스트")
    public void verifyCodeUserTest(){
        User user = createUser();
        userRepository.save(user);

        VerifyCode verifyCode = new VerifyCode();
        verifyCode.setVerifyCode("abcd1234");

        verifyCodeRepository.save(verifyCode);

        em.flush();
        em.clear();

        VerifyCode saveVerifyCode = verifyCodeRepository
                .findById(verifyCode.getId())
                .orElseThrow(EntityNotFoundException::new);

        //assertEquals(saveVerifyCode.getUser().getUserId(),user.getUserId());

    }
}