package com.hoxy.hoxymall.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UpdateProduct {

    private Long productId;

    private String productName;

    private String description;

    private int price;

    private int quantity;

    private List<String> category;
    private List<Long> categoryIds;

    private String imgUrl;

}
