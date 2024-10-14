package com.hoxy.hoxymall.service;

import com.hoxy.hoxymall.dto.CategoryDTO;
import com.hoxy.hoxymall.entity.Category;
import com.hoxy.hoxymall.exception.CategoryAlreadyExistsException;
import com.hoxy.hoxymall.repository.CategoryRepository;
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


}
