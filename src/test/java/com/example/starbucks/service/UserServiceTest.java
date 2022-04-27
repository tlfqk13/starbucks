package com.example.starbucks.service;

import com.example.starbucks.dto.UserFormDto;
import com.example.starbucks.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

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
    @DisplayName("회원가입 테스트")

    public void saveMemberTest(){
        User user = createUser();
        User savedUser = userService.saveUser(user);

        assertEquals(user.getUserId(),savedUser.getUserId());
        assertEquals(user.getPassword(),savedUser.getPassword());
        assertEquals(user.getUserName(),savedUser.getUserName());
        assertEquals(user.getNickName(),savedUser.getNickName());
        assertEquals(user.getBirthDate(),savedUser.getBirthDate());
        assertEquals(user.getAddress(),savedUser.getAddress());
        assertEquals(user.getEmail(),savedUser.getEmail());
        assertEquals(user.getPhoneNumber(),savedUser.getPhoneNumber());
    }
    
    @Test
    @DisplayName("중복 회원 가입 테스트")
    public void saveDuplicateUserTest(){
        User user1 = createUser();
        User user2 = createUser();
        userService.saveUser(user1);

        Throwable e = assertThrows(IllegalStateException.class,()->{
            userService.saveUser(user2);});

        assertEquals("이미 가입된 회원입니다", e.getMessage());
        }
}
