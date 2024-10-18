package com.hoxy.hoxymall.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class DescriptionImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long descriptionImgId;

    private String descriptionImgUrl;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;


    // Product와 URL을 받아 초기화하는 생성자
    public DescriptionImage(String descriptionImgUrl, Product product) {
        this.descriptionImgUrl = descriptionImgUrl;
        this.product = product;
    }
}
