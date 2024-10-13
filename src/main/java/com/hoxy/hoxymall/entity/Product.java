package com.hoxy.hoxymall.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    private String productName;

    private String description;

    private int price;

    private int quantity;

    //상품 sku(난수로 생성)
    private String sku;

    private String category;

    private String imgUrl;

    //현재시간 등록
    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;
}

