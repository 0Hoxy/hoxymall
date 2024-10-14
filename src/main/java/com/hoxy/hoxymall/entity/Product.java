package com.hoxy.hoxymall.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToMany
    private List<Category> categories = new ArrayList<>();

    private String imgUrl;

    //현재시간 등록
    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;
}

