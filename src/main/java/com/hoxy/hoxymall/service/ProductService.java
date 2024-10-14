package com.hoxy.hoxymall.service;

import com.hoxy.hoxymall.dto.AddProduct;
import com.hoxy.hoxymall.dto.GetProduct;
import com.hoxy.hoxymall.dto.ProductListDTO;
import com.hoxy.hoxymall.dto.UpdateProduct;
import com.hoxy.hoxymall.entity.Category;
import com.hoxy.hoxymall.entity.Product;
import com.hoxy.hoxymall.repository.CategoryRepository;
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

    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;

    private final ModelMapper modelMapper;
    public void addProduct(AddProduct addProduct) {
        Product product = modelMapper.map(addProduct, Product.class);
        //SKU 생성
        String sku = SkuGenerator.generateSku(8);
        product.setSku(sku);

        product.setCreatedDate(LocalDateTime.now());

        //카테고리 설정
        List<Category> categories = categoryRepo.findAllByCategoryIdIn(addProduct.getCategoryIds());
        product.setCategories(categories);
        System.out.println(categories);

        productRepo.save(product);
    }

    public List<ProductListDTO> showProduct() {
        return productRepo.findAll()
                .stream()
                .map(product -> new ProductListDTO(
                        product.getProductId(),
                        product.getProductName(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getCategories().stream()
                                .map(Category::getCategoryName)
                                .collect(Collectors.toList()) // 카테고리 이름 리스트로 변환
                ))
                .collect(Collectors.toList());
    }

    public GetProduct getProductById(Long id) {
        // 상품을 조회하여 Optional<Product>을 받습니다.
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 상품이 존재하지 않습니다."));

        List<String> categoryNames = product.getCategories().stream()
                .map(Category::getCategoryName)
                .collect(Collectors.toList());
        // Product 객체를 GetProduct DTO로 변환하여 반환합니다.
        return new GetProduct(
                product.getProductId(),
                product.getProductName(),
                product.getDescription(),
                product.getPrice(),
                categoryNames,
                product.getImgUrl()
        );
    }

    public UpdateProduct getUpdateProductId(Long id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 상품이 존재하지 않습니다."));

        List<String> categoryNames = product.getCategories().stream().map(Category::getCategoryName).collect(Collectors.toList());
        List<Long> categoryIds = product.getCategories().stream().map(Category::getCategoryId).collect(Collectors.toList());
        return new UpdateProduct(
                product.getProductId(),
                product.getProductName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity(),
                categoryNames,
                categoryIds,
                product.getImgUrl()
        );
    }

    public void updateProduct(Long id, UpdateProduct updateProduct) {
        Product product = productRepo.findByProductId(id);
        if (product == null) {
            throw new EntityNotFoundException("해당 상품이 존재하지 않습니다.");
        }

        product.setProductName(updateProduct.getProductName());
        product.setDescription(updateProduct.getDescription());
        product.setPrice(updateProduct.getPrice());
        product.setQuantity(updateProduct.getQuantity());
        //카테고리 업데이트
        List<Category> categories = categoryRepo.findAllByCategoryIdIn(updateProduct.getCategoryIds()); // 주어진 카테고리 ID 목록에 해당하는 모든 엔티티를 찾는 메서드
        product.setCategories(categories);

        product.setImgUrl(updateProduct.getImgUrl());
        product.setUpdatedDate(LocalDateTime.now());

        productRepo.save(product);
    }

    public void deleteProduct(Long id) {
        Product product = productRepo.findById(id).orElseThrow(() -> new RuntimeException("해당 상품이 존재하지 않습니다"));
        productRepo.delete(product);
    }
}
