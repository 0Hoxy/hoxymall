package com.hoxy.hoxymall.repository;

import com.hoxy.hoxymall.entity.Category;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByCategoryIdIn(List<Long> categoryIds);

    Boolean existsByCategoryName(String CategoryName); //실체 객체를 반환하는 것이 아닌 조건이 충족되는지 확인 해야하기 때문에 Boolean을 사용

    default Category findCategoryOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new EntityNotFoundException("해당 카테고리를 찾을 수 없습니다."));
    }
}
