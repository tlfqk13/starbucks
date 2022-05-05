package com.example.starbucks.entity;


import com.example.starbucks.constant.ItemSellStatus;
import com.example.starbucks.exception.OutOfStockException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
public class Item extends BaseEntity {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 50)
    private String itemName;

    @Column(name="price", nullable = false)
    private int price;

    @Column(nullable = false)
    private int stockNumber;

    @Lob
    @Column(nullable = false)
    private String itemDetail;

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;

    // 상품의 재고를 감소시키는 로직
    /*
    * 1. 상품의 재고 수량에서 주문 후 남은 재고 수량을 구함
    * 2. 상품의 재고가 주문수량보다 작을 경우 재고 수량 부족 Exception 발생
    * 3. 주문 후 남은 재고 수량을 상품의 현재 재고 값으로 업데이트
    *
    * */

    public void removeStock(int stockNumber){
        int restStock = this.stockNumber -stockNumber;
        if(restStock < 0){
            throw new OutOfStockException("상품의 재고가 부족합니다. (현재 재고 수량 : " + this.stockNumber + ")");
        }
        this.stockNumber = restStock;
    }

    // 주문을 취소 기능 구현
    // 취소하면 상품의 재고를 증가시키는 메소드
    public void addStock(int stockNumber){
        this.stockNumber += stockNumber;
    }
}
















