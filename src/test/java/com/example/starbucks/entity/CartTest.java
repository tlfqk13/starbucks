package com.example.starbucks.entity;

import com.example.starbucks.dto.UserFormDto;
import com.example.starbucks.repository.CartRepository;
import com.example.starbucks.repository.UserRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class CartTest {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

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
    @DisplayName("장바구니 회원 엔티티 매핑 조회 테스트")
    public void findCartAndUserTest(){
        User user = createUser();
        userRepository.save(user);

        Cart cart = new Cart();
        cart.setUser(user);
        cartRepository.save(cart);

        em.flush();
        em.clear();

        Cart savedCart = cartRepository.findById(cart.getId()).orElseThrow(EntityNotFoundException::new);

        assertEquals(savedCart.getUser().getId(),user.getId());

    }
}






















