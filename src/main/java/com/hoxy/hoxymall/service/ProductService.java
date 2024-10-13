package com.hoxy.hoxymall.service;

import com.hoxy.hoxymall.dto.AddProduct;
import com.hoxy.hoxymall.dto.GetProduct;
import com.hoxy.hoxymall.dto.ProductListDTO;
import com.hoxy.hoxymall.dto.UpdateProduct;
import com.hoxy.hoxymall.entity.Product;
import com.hoxy.hoxymall.repository.ProductRepository;
import com.hoxy.hoxymall.util.SkuGenerator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repo;

    private final ModelMapper modelMapper;
    public void addProduct(AddProduct addProduct) {
        Product product = modelMapper.map(addProduct, Product.class);
        String sku = SkuGenerator.generateSku(8);
        product.setSku(sku);
        product.setCreatedDate(LocalDateTime.now());
        repo.save(product);
    }

    public List<ProductListDTO> showProduct() {
        return repo.findAll()
                .stream()
                .map(product -> new ProductListDTO(
                        product.getProductId(),
                        product.getProductName(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getCategory()
                ))
                .collect(Collectors.toList());
    }

    public GetProduct getProductById(Long id) {
        // 상품을 조회하여 Optional<Product>을 받습니다.
        Product product = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 상품이 존재하지 않습니다."));

        // Product 객체를 GetProduct DTO로 변환하여 반환합니다.
        return new GetProduct(
                product.getProductId(),
                product.getProductName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory(),
                product.getImgUrl()
        );
    }

    public UpdateProduct getUpdateProductId(Long id) {
        Product product = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 상품이 존재하지 않습니다."));
        return new UpdateProduct(
                product.getProductId(),
                product.getProductName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity(),
                product.getCategory(),
                product.getImgUrl()
        );
    }

    public void updateProduct(Long id, UpdateProduct updateProduct) {
        Product product = repo.findByProductId(id);
        if (product == null) {
            throw new EntityNotFoundException("해당 상품이 존재하지 않습니다.");
        }

        product.setProductName(updateProduct.getProductName());
        product.setDescription(updateProduct.getDescription());
        product.setPrice(updateProduct.getPrice());
        product.setQuantity(updateProduct.getQuantity());
        product.setCategory(updateProduct.getCategory());
        product.setImgUrl(updateProduct.getImgUrl());
        product.setUpdatedDate(LocalDateTime.now());

        repo.save(product);
    }

    public void deleteProduct(Long id) {
        Product product = repo.findById(id).orElseThrow(() -> new RuntimeException("해당 상품이 존재하지 않습니다"));
        repo.delete(product);
    }
}
