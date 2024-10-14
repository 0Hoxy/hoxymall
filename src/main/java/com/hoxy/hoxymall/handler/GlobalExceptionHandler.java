package com.hoxy.hoxymall.handler;

import com.hoxy.hoxymall.exception.CategoryAlreadyExistsException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CategoryAlreadyExistsException.class)
    public String handleCategoryAlreadyExists(CategoryAlreadyExistsException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "redirect:/categories/add"; // 카테고리 등록 페이지로 리다이렉트
    }
}

