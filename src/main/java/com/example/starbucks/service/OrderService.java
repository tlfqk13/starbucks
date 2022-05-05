package com.example.starbucks.service;


import com.example.starbucks.dto.OrderDto;
import com.example.starbucks.dto.OrderHistDto;
import com.example.starbucks.dto.OrderItemDto;
import com.example.starbucks.entity.*;
import com.example.starbucks.repository.ItemImgRepository;
import com.example.starbucks.repository.ItemRepository;
import com.example.starbucks.repository.OrderRepository;
import com.example.starbucks.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ItemImgRepository itemImgRepository;

    /*
    * 1. 주문 상품 조회
    * 2. 현재 로그인한 회원의 아이디를 이용해서 회원 정보를 조회
    * 3. 주문할 상품 엔티티와 주문 수량을 이용하여 주문 상품 엔티티 생성
    * 4. 회원 정보와 주문 상품 리스트 정보를 이용하여 주문 엔티티 생성
    * 5. 생성한 주문 엔티티 저장
    * */

    public Long order(OrderDto orderDto,String userId){
        Item item = itemRepository.findById(orderDto.getItemId()).orElseThrow(EntityNotFoundException::new);
        User user = userRepository.findByUserId(userId);

        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItem = OrderItem.createOrderItem(item,orderDto.getCount());
        orderItemList.add(orderItem);

        Order order = Order.createOrder(user,orderItemList);
        orderRepository.save(order);

        return order.getId();
    }

    // 주문 목록 조회
    @Transactional(readOnly = true)
    public Page<OrderHistDto> getOrderList(String userId, Pageable pageable){

        List<Order> orders = orderRepository.findOrders(userId, pageable);
        Long totalCount = orderRepository.countOrder(userId);

        List<OrderHistDto> orderHistDtos = new ArrayList<>();
        
        /*
        * 1. 유저의 아이디와 페이징 조건을 이용하여 주문 목록을 조회
        * 2. 유저의 총 주문 개수를 구함
        * 3. 주문 리스트를 순회하면서 구매 이력 페이지에 전달할 DTO 생성
        * 4. 주문한 상품의 대표 이미지 조회
        * 5. 페이지 구현 객체 리턴
        * */

        for(Order order : orders){
            OrderHistDto orderHistDto = new OrderHistDto(order);
            List<OrderItem> orderItems = order.getOrderItems();

            for(OrderItem orderItem : orderItems){
                ItemImg itemImg = itemImgRepository.findByItemIdAndRepimgYn(orderItem.getItem().getId(),"Y");
                OrderItemDto orderItemDto = new OrderItemDto(orderItem,itemImg.getImgUrl());
                orderHistDto.addOrderItemDto(orderItemDto);
            }
            orderHistDtos.add(orderHistDto);
        }
        return new PageImpl<OrderHistDto>(orderHistDtos,pageable,totalCount);
    }

    // 주문 취소
    @Transactional(readOnly = true)
    public boolean validateOrder(Long orderId, String userId){
        /*
        * 1. 현재 로그인한 사용자와 주문 데이터를 생성한 사용자가 같은지 검사
        *
        * */
        User curUser = userRepository.findByUserId(userId);
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        User savedUser = order.getUser();

        if(!StringUtils.equals(curUser.getUserId(),savedUser.getUserId())){
            return false;
        }
        return true;
    }

    public void cancelOrder(Long orderId){
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        order.cancelOrder();
    }

    public Long orders(List<OrderDto> orderDtoList, String userId){

        User user = userRepository.findByEmail(userId);
        List<OrderItem> orderItemList = new ArrayList<>();

        for(OrderDto orderDto : orderDtoList){
            Item item = itemRepository.findById(orderDto.getItemId()).orElseThrow(EntityNotFoundException::new);

            OrderItem orderItem = OrderItem.createOrderItem(item,orderDto.getCount());
            orderItemList.add(orderItem);
        }

        Order order = Order.createOrder(user,orderItemList);
        orderRepository.save(order);

        return order.getId();
    }
}
















