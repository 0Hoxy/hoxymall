package com.hoxy.hoxymall.controller;

import com.hoxy.hoxymall.dto.*;
import com.hoxy.hoxymall.entity.Category;
import com.hoxy.hoxymall.service.CategoryService;
import com.hoxy.hoxymall.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/products")
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
    public String postProduct(Model model, AddProduct addProduct) {
        productService.addProduct(addProduct);
        model.addAttribute("message", "상품 등록이 완료되었습니다.");
        return "redirect:/products";
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
    public String postUpdateProduct(@PathVariable Long id, UpdateProduct updateProduct) {
        productService.updateProduct(id, updateProduct);
        return "redirect:/products";
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }
}
