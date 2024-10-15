package com.hoxy.hoxymall.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productImgId;

    private String productImgUrl;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;


    // Product와 URL을 받아 초기화하는 생성자
    public ProductImage(String productImgUrl, Product product) {
        this.productImgUrl = productImgUrl;
        this.product = product;
    }

}
