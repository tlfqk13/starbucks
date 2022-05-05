package com.example.starbucks.service;

import com.example.starbucks.constant.ItemSellStatus;
import com.example.starbucks.dto.CartItemDto;
import com.example.starbucks.entity.CartItem;
import com.example.starbucks.entity.Item;
import com.example.starbucks.entity.User;
import com.example.starbucks.repository.CartItemRepository;
import com.example.starbucks.repository.ItemRepository;
import com.example.starbucks.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class CartServiceTest {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CartService cartService;

    @Autowired
    CartItemRepository cartItemRepository;

    public Item saveItem(){
        Item item = new Item();
        item.setItemName("테스트상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 디테일");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        return itemRepository.save(item);
    }

    public User saveUser(){
        User user = new User();
        user.setUserId("test");
        return userRepository.save(user);
    }
    
    @Test
    @DisplayName("장바구니 담기 테스트")
    public void addCart(){
        Item item = saveItem();
        User user = saveUser();

        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setCount(5);
        cartItemDto.setItemId(item.getId());

        Long cartItemId = cartService.addCart(cartItemDto,user.getUserId());

        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);

        assertEquals(item.getId(),cartItem.getItem().getId());
        assertEquals(cartItemDto.getCount(),cartItem.getCount());
    }

}