package com.hoxy.hoxymall.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetProduct {
    private Long productId;

    private String productName;

    private String description;

    private int price;

    private List<String> categoryName;

    private List<String> productImgUrl;

    private List<String> descriptionImgUrl;

}
