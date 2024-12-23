package com.hoxy.hoxymall.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddProduct {

    private String productName;

    private String description;

    private int price;

    private int quantity;

    private List<Long> categoryIds;

    private List<MultipartFile> productImgFiles;
    private List<MultipartFile> descriptionImgFiles;


}
