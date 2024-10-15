package com.hoxy.hoxymall.repository;

import com.hoxy.hoxymall.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {
    Product findByProductId(Long id);

    Product findSkuByProductId(Long id);
}
