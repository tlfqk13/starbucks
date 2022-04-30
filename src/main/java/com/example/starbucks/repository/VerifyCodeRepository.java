package com.example.starbucks.repository;


import com.example.starbucks.entity.VerifyCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerifyCodeRepository extends JpaRepository<VerifyCode,Long> {

}
