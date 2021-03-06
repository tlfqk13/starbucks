package com.example.starbucks.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.inheritance.C;

import javax.persistence.*;

@Entity
@Table(name="cart")
@Getter
@Setter
@ToString
public class Cart {

    @Id
    @Column(name="cart_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name="user_id")
    private User user;

    public static Cart createCart(User user){
        Cart cart = new Cart();
        cart.setUser(user);

        return cart;
    }

}
