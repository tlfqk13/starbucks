package com.example.starbucks.entity;


import com.example.starbucks.dto.CartItemDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter@Setter
@Table(name="cart_item")
public class CartItem extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name="cart_item_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name="item_id")
    private Item item;

    private int count;
    
    // 장바구니에 담을 상품 엔티티 생성
    public static CartItem createCartItem(Cart cart, Item item, int count){
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setItem(item);
        cartItem.setCount(count);

        return cartItem;
    }
    
    // 장바구니에 담을 수량을 증가시켜주는 엔티티 생성
    // 장바구니에 기존에 담겨 있는 상품인데 해당 상품을 추가로 담을때 기존 수량에 더해준다
    public void addCount(int count){
        this.count += count;
    }
    
    // 장바구니 상품 수량 변경하기
    public void updateCount(int count){
        this.count = count;
    }

}
