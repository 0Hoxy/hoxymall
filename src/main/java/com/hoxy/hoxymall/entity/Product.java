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
    @JoinTable(
            name = "product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categories = new ArrayList<>();

    // 상품 이미지 연관 관계 추가
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductImage> productImages = new ArrayList<>();

    // 상세 설명 이미지 연관 관계 추가
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<DescriptionImage> descriptionImages = new ArrayList<>();

    //현재시간 등록
    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;
}

