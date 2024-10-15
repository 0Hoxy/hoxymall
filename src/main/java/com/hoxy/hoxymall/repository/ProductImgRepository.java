package com.hoxy.hoxymall.repository;

import com.hoxy.hoxymall.entity.Category;
import com.hoxy.hoxymall.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImgRepository extends JpaRepository<ProductImage, Long> {
    List<ProductImage> findAllByProductImgIdIn(List<Long> productImgId);
}

