package com.hoxy.hoxymall.service;

import com.hoxy.hoxymall.dto.*;
import com.hoxy.hoxymall.entity.Category;
import com.hoxy.hoxymall.entity.DescriptionImage;
import com.hoxy.hoxymall.entity.Product;
import com.hoxy.hoxymall.entity.ProductImage;
import com.hoxy.hoxymall.repository.CategoryRepository;
import com.hoxy.hoxymall.repository.DescriptionImgRepository;
import com.hoxy.hoxymall.repository.ProductImgRepository;
import com.hoxy.hoxymall.repository.ProductRepository;
import com.hoxy.hoxymall.util.SkuGenerator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;
    private final ModelMapper modelMapper;
    private final ProductImgRepository productImgRepository;
    private final DescriptionImgRepository descriptionImgRepository;
    private final ImageService imageService; // ImageService 주입

    public void addProduct(AddProduct addProduct) {
        Product product = modelMapper.map(addProduct, Product.class);
        product.setSku(SkuGenerator.generateSku(8));
        product.setCreatedDate(LocalDateTime.now());

        // 카테고리 설정
        product.setCategories(findCategoriesByIds(addProduct.getCategoryIds()));

        // 이미지 업로드 및 설정
        List<ProductImageDTO> productImageDTOs = uploadImages(addProduct.getProductImgFiles());
        List<ProductImage> productImages = productImageDTOs.stream()
                .map(dto -> new ProductImage(dto.getProductImgUrl(), product)) // ProductImage 생성 시 product 필드 설정
                .collect(Collectors.toList());
        product.setProductImages(productImages);

        List<DescriptionImageDTO> descriptionImageDTOs = uploadDescriptionImages(addProduct.getDescriptionImgFiles());
        List<DescriptionImage> descriptionImages = descriptionImageDTOs.stream()
                .map(dto -> new DescriptionImage(dto.getDescriptionImgUrl(), product)) // DescriptionImage 생성 시 product 필드 설정
                .collect(Collectors.toList());
        product.setDescriptionImages(descriptionImages);


        productRepo.save(product);
    }


    // 이미지 업로드 및 ProductImage 생성
    private List<ProductImageDTO> uploadImages(List<MultipartFile> productImgFiles) {
        return productImgFiles.stream()
                .map(file -> {
                    String imgUrl = imageService.uploadImg(file);
                    return new ProductImageDTO(imgUrl); // ProductImageDTO 생성
                })
                .collect(Collectors.toList());
    }

    // 상세 이미지 업로드 및 DescriptionImage 생성
    private List<DescriptionImageDTO> uploadDescriptionImages(List<MultipartFile> descriptionImgFiles) {
        return descriptionImgFiles.stream()
                .map(file -> {
                    String imgUrl = imageService.uploadImg(file);
                    return new DescriptionImageDTO(imgUrl); // DescriptionImageDTO 생성
                })
                .collect(Collectors.toList());
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
                                .collect(Collectors.toList()),
                        product.getProductImages().stream()
                                .map(ProductImage::getProductImgUrl)
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

    public GetProduct getProductById(Long id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 상품이 존재하지 않습니다."));

        return mapProductToGetProduct(product);
    }

    public UpdateProduct getUpdateProductId(Long id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 상품이 존재하지 않습니다."));

        return mapProductToUpdateProduct(product);
    }

    @Transactional
    public void updateProduct(Long id, UpdateProduct updateProduct) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 상품이 존재하지 않습니다."));

        // 기존 이미지 삭제 및 파일 시스템에서 삭제
        imageService.deleteImagesFromFileSystem(product.getProductImages());

        // 기존 이미지를 명시적으로 삭제
        product.getProductImages().forEach(image -> {
            image.setProduct(null);  // 연관관계 제거
            productImgRepository.delete(image);  // 이미지 삭제
        });
        product.getProductImages().clear();  // 컬렉션 비우기

        // 제품 정보 업데이트
        product.setProductName(updateProduct.getProductName());
        product.setDescription(updateProduct.getDescription());
        product.setPrice(updateProduct.getPrice());
        product.setQuantity(updateProduct.getQuantity());
        product.setCategories(findCategoriesByIds(updateProduct.getCategoryIds()));

        // 이미지 파일이 제공된 경우에만 업로드 및 업데이트
        if (updateProduct.getProductImgFiles() != null && !updateProduct.getProductImgFiles().isEmpty()) {
            List<ProductImage> newProductImages = uploadImages(updateProduct.getProductImgFiles()).stream()
                    .map(dto -> new ProductImage(dto.getProductImgUrl(), product)) // 엔티티 생성
                    .collect(Collectors.toList());
            product.setProductImages(newProductImages);
        }

        // 설명 이미지 처리
        imageService.deleteDescriptionImagesFromFileSystem(product.getDescriptionImages());
        product.getDescriptionImages().forEach(image -> {
            image.setProduct(null);  // 연관관계 제거
            descriptionImgRepository.delete(image);  // 이미지 삭제
        });
        product.getDescriptionImages().clear();  // 컬렉션 비우기

        if (updateProduct.getDescriptionImgFiles() != null && !updateProduct.getDescriptionImgFiles().isEmpty()) {
            List<DescriptionImage> newDescriptionImages = uploadDescriptionImages(updateProduct.getDescriptionImgFiles()).stream()
                    .map(dto -> new DescriptionImage(dto.getDescriptionImgUrl(), product)) // 엔티티 생성
                    .collect(Collectors.toList());
            product.setDescriptionImages(newDescriptionImages);
        }

        product.setUpdatedDate(LocalDateTime.now());
        productRepo.save(product);
    }





    public void deleteProduct(Long id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 상품이 존재하지 않습니다."));
        productRepo.delete(product);
    }

    // 카테고리 찾기
    private List<Category> findCategoriesByIds(List<Long> categoryIds) {
        return categoryRepo.findAllByCategoryIdIn(categoryIds);
    }

    // Product -> GetProduct DTO 변환
    private GetProduct mapProductToGetProduct(Product product) {
        // 카테고리 이름 리스트
        List<String> categoryNames = product.getCategories().stream()
                .map(Category::getCategoryName)
                .collect(Collectors.toList());

        // 제품 이미지 URL 리스트
        List<String> productImgUrls = product.getProductImages().stream()
                .map(ProductImage::getProductImgUrl) // imgUrl을 가져옵니다
                .collect(Collectors.toList());

        // 설명 이미지 URL 리스트
        List<String> descriptionImgUrls = product.getDescriptionImages().stream()
                .map(DescriptionImage::getDescriptionImgUrl) // imgUrl을 가져옵니다
                .collect(Collectors.toList());

        return new GetProduct(
                product.getProductId(),
                product.getProductName(),
                product.getDescription(),
                product.getPrice(),
                categoryNames,
                productImgUrls, // 제품 이미지 URL 리스트 추가
                descriptionImgUrls // 설명 이미지 URL 리스트 추가
        );
    }


    // Product -> UpdateProduct DTO 변환
    private UpdateProduct mapProductToUpdateProduct(Product product) {
        List<String> categoryNames = product.getCategories().stream()
                .map(Category::getCategoryName)
                .toList(); // collect(Collectors.toList()) 대신 toList() 사용

        List<Long> categoryIds = product.getCategories().stream()
                .map(Category::getCategoryId)
                .toList(); // collect(Collectors.toList()) 대신 toList() 사용

        return new UpdateProduct(
                product.getProductId(),
                product.getProductName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity(),
                categoryNames,
                categoryIds,
                null,
                null // image files는 업데이트 DTO에 없음
        );
    }

}
