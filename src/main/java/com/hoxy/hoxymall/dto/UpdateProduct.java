package com.hoxy.hoxymall.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class UpdateProduct {

    private Long productId;

    private String productName;

    private String description;

    private int price;

    private int quantity;

    private String category;

    private String imgUrl;

}
