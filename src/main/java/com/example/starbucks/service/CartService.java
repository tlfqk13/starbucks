package com.example.starbucks.service;

import com.example.starbucks.dto.CartDetailDto;
import com.example.starbucks.dto.CartItemDto;
import com.example.starbucks.dto.CartOrderDto;
import com.example.starbucks.dto.OrderDto;
import com.example.starbucks.entity.Cart;
import com.example.starbucks.entity.CartItem;
import com.example.starbucks.entity.Item;
import com.example.starbucks.entity.User;
import com.example.starbucks.repository.CartItemRepository;
import com.example.starbucks.repository.CartRepository;
import com.example.starbucks.repository.ItemRepository;
import com.example.starbucks.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final OrderService orderService;

    /*
    * 1. 장바구니에 담을 상품 엔티티 조회
    * 2. 현재 로그인한 회원 엔티티 조회
    * 3. 현재 로그인한 회원의 장바구니 엔티티를 조회
    * 4. 상품을 처음으로 담을경우 장바구니 생성 (장바구니 null 체크)
    * 5. 장바구니에 이미 있던 상품일 경우 기존 수량 + 담을 수량
    * 6. (장바구니 엔티티, 상품 엔티티, 장바구니에 담을 수량 )을 이용하여 CartItem 엔티티 생성
    * 7. 장바구니에 들어갈 상품 저장
    * */

    public Long addCart(CartItemDto cartItemDto, String userId){
        Item item = itemRepository.findById(cartItemDto.getItemId()).orElseThrow(EntityNotFoundException::new);
        User user = userRepository.findByUserId(userId);

        Cart cart = cartRepository.findByUserId(user.getId());
        if(cart == null){
            cart = Cart.createCart(user);
            cartRepository.save(cart);
        }

        CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(),item.getId());

        if(savedCartItem != null){
            savedCartItem.addCount(cartItemDto.getCount());
            return savedCartItem.getId();
        } else{
            CartItem cartItem = CartItem.createCartItem(cart,item, cartItemDto.getCount());
            cartItemRepository.save(cartItem);

            return cartItem.getId();
        }
    }

    @Transactional(readOnly = true)
    public List<CartDetailDto> getCartList(String userId){

        List<CartDetailDto> cartDetailDtoList = new ArrayList<>();

        User user = userRepository.findByUserId(userId);
        Cart cart = cartRepository.findByUserId(user.getId());
        if(cart == null){
            return cartDetailDtoList;
        }
        cartDetailDtoList = cartItemRepository.findCartDetailDtoList(cart.getId());

        return cartDetailDtoList;
    }

    @Transactional(readOnly = true)
    public boolean validateCartItem(Long cartItemId, String userId){
        // 장바구니 수량 업데이트
        /*
        * 1. 현재 로그인한 회원을 조회
        * 2. 장바구니 상품을 저장한 회원 조회
        * 3. 현재 로그인한 회원과 장바구니 상품을 조회한 회원이 체크
        * 4. 장바구니 수량 업데이트
        * */

        User curUser = userRepository.findByUserId(userId);
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);

        User savedUser = cartItem.getCart().getUser();

        if(!StringUtils.equals(curUser.getUserId(),savedUser.getUserId())){
            return false;
        }
        return true;
    }

    public void updateCartItemCount(Long cartItemId, int count){
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);

        cartItem.updateCount(count);
    }

    // 장바구니 상품 삭제하기
    public void deleteCartItem(Long cartItemId){
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        cartItemRepository.delete(cartItem);
    }

    public Long orderCartItem(List<CartOrderDto> cartOrderDtoList, String userId){
        List<OrderDto> orderDtoList = new ArrayList<>();
        for(CartOrderDto cartOrderDto : cartOrderDtoList){
            CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId()).orElseThrow(EntityNotFoundException::new);

            OrderDto orderDto = new OrderDto();
            orderDto.setItemId(cartItem.getItem().getId());
            orderDto.setCount(cartItem.getCount());
            orderDtoList.add(orderDto);
        }

        Long orderId = orderService.orders(orderDtoList,userId);

        for(CartOrderDto cartOrderDto : cartOrderDtoList){
            CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId()).orElseThrow(EntityNotFoundException::new);
            cartItemRepository.delete(cartItem);
        }

        return orderId;
    }
}











