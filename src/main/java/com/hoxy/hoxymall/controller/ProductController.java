package com.hoxy.hoxymall.controller;

import com.hoxy.hoxymall.dto.AddProduct;
import com.hoxy.hoxymall.dto.GetProduct;
import com.hoxy.hoxymall.dto.ProductListDTO;
import com.hoxy.hoxymall.dto.UpdateProduct;
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

    private final ProductService service;

    @GetMapping("/add")
    public String viewAddProductForm(Model model) {
        model.addAttribute("addProduct", new AddProduct());
        return "addProduct";
    }

    @PostMapping("/add")
    public String postProduct(Model model, AddProduct addProduct) {
        service.addProduct(addProduct);
        model.addAttribute("message", "상품 등록이 완료되었습니다.");
        return "redirect:/products";
    }

    @GetMapping
    public String getAllProducts(Model model) {
        List<ProductListDTO> productList = service.showProduct();
        model.addAttribute("products", productList);
        return "productList";
    }

    @GetMapping("/{id}")
    public String getProduct(@PathVariable Long id, Model model) {
        GetProduct product = service.getProductById(id);
        model.addAttribute("product", product);
        return "productDetail";
    }

    @GetMapping("/update/{id}")
    public String updateProduct(@PathVariable Long id, Model model) {
        UpdateProduct product = service.getUpdateProductId(id);
        model.addAttribute("product", product);
        return "productUpdate";
    }

    @PostMapping("/update/{id}")
    public String postUpdateProduct(@PathVariable Long id, UpdateProduct updateProduct) {
        service.updateProduct(id, updateProduct);
        return "redirect:/products";
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        service.deleteProduct(id);
        return "redirect:/products";
    }
}
