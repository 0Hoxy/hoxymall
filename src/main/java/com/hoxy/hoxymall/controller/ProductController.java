package com.hoxy.hoxymall.controller;

import com.hoxy.hoxymall.dto.*;
import com.hoxy.hoxymall.entity.Category;
import com.hoxy.hoxymall.service.CategoryService;
import com.hoxy.hoxymall.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("admin/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping("/add")
    public String viewAddProductForm(Model model) {
        List<Category> categories = categoryService.findAll();
        model.addAttribute("addProduct", new AddProduct());
        model.addAttribute("categories", categories);
        return "addProduct";
    }

    @PostMapping("/add")
    public String postProduct(Model model,
                              @ModelAttribute AddProduct addProduct,
                              @RequestParam("productImgFiles") List<MultipartFile> productImgFiles, // 이미지 파일
                              @RequestParam("descriptionImgFiles") List<MultipartFile> descriptionImgFiles) { // 상세 이미지 파일
        addProduct.setProductImgFiles(productImgFiles);
        addProduct.setDescriptionImgFiles(descriptionImgFiles);

        productService.addProduct(addProduct);
        model.addAttribute("message", "상품 등록이 완료되었습니다.");
        return "redirect:/admin/products";
    }

    @GetMapping
    public String getAllProducts(Model model) {
        List<ProductListDTO> productList = productService.showProduct();
        model.addAttribute("products", productList);
        return "productList";
    }

    @GetMapping("/{id}")
    public String getProduct(@PathVariable Long id, Model model) {
        GetProduct product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "productDetail";
    }

    @GetMapping("/update/{id}")
    public String updateProduct(@PathVariable Long id, Model model) {
        List<Category> categories = categoryService.findAll();
        UpdateProduct product = productService.getUpdateProductId(id);
        model.addAttribute("product", product);
        model.addAttribute("categories", categories);
        return "productUpdate";
    }

    @PostMapping("/update/{id}")
    public String postUpdateProduct(@PathVariable Long id,
                                    @ModelAttribute UpdateProduct updateProduct,
                                    @RequestParam("productImgFiles") List<MultipartFile> productImgFiles, // 이미지 파일
                                    @RequestParam("descriptionImgFiles") List<MultipartFile> descriptionImgFiles) { // 상세 이미지 파일
        updateProduct.setProductImgFiles(productImgFiles);
        updateProduct.setDescriptionImgFiles(descriptionImgFiles);

        productService.updateProduct(id, updateProduct);
        return "redirect:/admin/products";
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }
}
