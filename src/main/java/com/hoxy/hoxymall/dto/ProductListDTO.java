package com.hoxy.hoxymall.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductListDTO {

    private Long productId;

    private String productName;

    private String description;

    private int price;

    private String category;
}
