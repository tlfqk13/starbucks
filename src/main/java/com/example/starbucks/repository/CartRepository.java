package com.example.starbucks.repository;

import com.example.starbucks.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart,Long> {

    Cart findByUserId(Long userId);
}
