package com.example.starbucks.controller;


import com.example.starbucks.dto.OrderDto;
import com.example.starbucks.dto.OrderHistDto;
import com.example.starbucks.entity.Order;
import com.example.starbucks.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping(value = "/order")
    public @ResponseBody
    ResponseEntity order(@RequestBody @Valid OrderDto orderDto, BindingResult bindingResult, Principal principal){
        // 스프링에서 비동기 처리를 할때 @RequestBody 와 @ResponseBody 어노테이션 사용
        /*
        * @RequestBody : HTTP 요청의 본문 body에 담긴 내용을 자바 객체로 전달
        * 
        * @ResponseBody : 자바객체를 HTTP요청의 body로 전달
        * 
        * */

        if(bindingResult.hasErrors()){
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for(FieldError fieldError : fieldErrors){
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        String userId = principal.getName();
        Long orderId;

        try{
            orderId = orderService.order(orderDto,userId);
        }catch (Exception e){
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Long>(orderId,HttpStatus.OK);
    }
    
    // 한 번에 가지고 올 주문의 개수는 4개로 설정
    // 현재 로그인한 회원은 아이디와 페이징 객체를 파라미터로 전달하여 
    // 화면에 전달한 주문 목록 데이터를 리턴 값으로 받는다

    @GetMapping(value = {"/orders","/orders/{page}"})
    public String orderHist(@PathVariable("page")Optional<Integer> page, Principal principal, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ?page.get():0,4);

        Page<OrderHistDto> orderHistDtoList = orderService.getOrderList(principal.getName(),pageable);

        model.addAttribute("orders",orderHistDtoList);
        model.addAttribute("page",pageable.getPageNumber());
        model.addAttribute("maxPage",5);

        return "order/orderHist";
    }

    @PostMapping("/order/{orderId}/cancel")
    public @ResponseBody ResponseEntity cancelOrder(@PathVariable("orderId")Long orderId, Principal principal){

        if(!orderService.validateOrder(orderId, principal.getName())){
            return new ResponseEntity<String>("주문 취소 권한이 없습니다. ",HttpStatus.FORBIDDEN );
        }

        orderService.cancelOrder(orderId);
        return new ResponseEntity<Long>(orderId,HttpStatus.OK);
    }
}
