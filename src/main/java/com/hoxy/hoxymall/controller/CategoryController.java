package com.hoxy.hoxymall.controller;

import com.hoxy.hoxymall.dto.CategoryDTO;
import com.hoxy.hoxymall.entity.Category;
import com.hoxy.hoxymall.exception.CategoryAlreadyExistsException;
import com.hoxy.hoxymall.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("admin/categories")
public class CategoryController {


    private final CategoryService categoryService;

    @GetMapping("/add")
    public String showAddCategory(Model model) {
        List<Category> categories = categoryService.findAll();
        model.addAttribute("category", new CategoryDTO());
        model.addAttribute("categories", categories);
        return "addCategory";
    }

    @PostMapping("/add")
    public String addCategory(CategoryDTO categoryDTO, RedirectAttributes redirectAttributes) {
        try {
            categoryService.addCategory(categoryDTO);
            redirectAttributes.addFlashAttribute("message", "카테고리 등록이 완료되었습니다."); // 성공 메시지 설정
        } catch (CategoryAlreadyExistsException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage()); // 에러 메시지 설정
        }
        return "redirect:/admin/categories/add"; // 카테고리 등록 페이지로 리다이렉트
    }

    @GetMapping("/update/{id}")
    public String showUpdateCategory(@PathVariable Long id, Model model) {
        List<Category> categories = categoryService.findAll();
        model.addAttribute("category", categoryService.getUpdateCategory(id)); // 업데이트할 카테고리 정보
        model.addAttribute("categories", categories); // 모든 카테고리 정보
        return "updateCategory"; // 템플릿 이름
    }


    @PostMapping("/update/{id}")
    public String updateCategory(@PathVariable Long id, @ModelAttribute CategoryDTO categoryDTO, Model model) {
        try {
            categoryService.updateCategory(id, categoryDTO);
            model.addAttribute("message", "카테고리 수정이 완료되었습니다.");
            return "redirect:/admin/categories/add"; // 카테고리 목록 페이지로 리다이렉트
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("category", categoryDTO); // 수정 페이지로 현재 DTO 정보를 재전달
            return "updateCategory"; // 수정 페이지로 다시 이동
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return "redirect:/admin/categories/add";
    }

}

