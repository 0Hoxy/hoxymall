package com.hoxy.hoxymall.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ProductListDTO {

    private Long productId;

    private String productName;

    private String description;

    private int price;

    private List<String> category;

    private List<String> productImgUrl;
}
