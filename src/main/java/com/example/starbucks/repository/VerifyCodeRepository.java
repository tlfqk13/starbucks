package com.example.starbucks.repository;


import com.example.starbucks.entity.VerifyCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VerifyCodeRepository extends JpaRepository<VerifyCode,Long> {

    VerifyCode findByVerifyCode(String token);

    @Query("select v from VerifyCode v where v.verifyCode = :userEmail")
    VerifyCode findByUserEmail(@Param(value = "userEmail") String userEmail);
}
