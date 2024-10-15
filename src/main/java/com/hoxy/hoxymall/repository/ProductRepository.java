package com.hoxy.hoxymall.repository;

import com.hoxy.hoxymall.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository<Product,Long> {

}
