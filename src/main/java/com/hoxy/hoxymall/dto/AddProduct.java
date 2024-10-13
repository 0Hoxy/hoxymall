package com.hoxy.hoxymall.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddProduct {

    private String productName;

    private String description;

    private int price;

    private int quantity;

    private String category;

    private String imgUrl;


}
