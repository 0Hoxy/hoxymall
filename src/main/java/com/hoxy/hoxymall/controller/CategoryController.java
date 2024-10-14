package com.hoxy.hoxymall.controller;

import com.hoxy.hoxymall.dto.CategoryDTO;
import com.hoxy.hoxymall.entity.Category;
import com.hoxy.hoxymall.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CategoryController {


    private final CategoryService categoryService;

    @GetMapping("/categories/add")
    public String showAddCategory(Model model) {
        List<Category> categories = categoryService.findAll();
        model.addAttribute("category", new CategoryDTO());
        model.addAttribute("categories", categories);
        return "addCategory";
    }

    @PostMapping("/categories/add")
    public String addCategory(Model model, CategoryDTO categoryDTO) {
        categoryService.addCategory(categoryDTO);
        model.addAttribute("message", "카테고리 등록이 완료되었습니다.");
        return "redirect:/categories/add";
    }


}
