package com.hoxy.hoxymall.service;

import com.hoxy.hoxymall.dto.CategoryDTO;
import com.hoxy.hoxymall.entity.Category;
import com.hoxy.hoxymall.exception.CategoryAlreadyExistsException;
import com.hoxy.hoxymall.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepo;
    private final ModelMapper modelMapper;

    public void addCategory(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO, Category.class);
        // 카테고리 이름이 이미 존재하는지 체크
        if (categoryRepo.existsByCategoryName(category.getCategoryName())) {
            throw new CategoryAlreadyExistsException("이미 존재하는 카테고리 입니다.");
        }
        categoryRepo.save(category);
    }

    public List<Category> findAll() {
        return categoryRepo.findAll();
    }

    public void updateCategory(Long id, CategoryDTO categoryDTO) {
        Category category = categoryRepo.findCategoryOrThrow(id);

        if (categoryRepo.existsByCategoryName(categoryDTO.getCategoryName())) { //category로 되어있어서 입력받은 DTO값이 아닌 입력되어있는 그대로의 상태로 조건문이 실행됐다
            throw new CategoryAlreadyExistsException("이미 존재하는 카테고리 입니다.");
        }

        category.setCategoryName(categoryDTO.getCategoryName());

        categoryRepo.save(category);
    }

    public CategoryDTO getUpdateCategory(Long id) {
        Category category = categoryRepo.findCategoryOrThrow(id);

        return modelMapper.map(category, CategoryDTO.class);
    }

    public void deleteCategory(Long id) {
        Category category = categoryRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("해당 카테고리를 찾을 수 없습니다."));
        categoryRepo.delete(category);
    }
}
