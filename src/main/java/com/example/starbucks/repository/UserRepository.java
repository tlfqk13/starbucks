package com.example.starbucks.repository;

import com.example.starbucks.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

    // 중복검사를 위한
    User findByUserId(String userId);
    User findByEmail(String email);
    User findByNickName(String nickName);
}
