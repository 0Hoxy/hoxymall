package com.hoxy.hoxymall.repository;

import com.hoxy.hoxymall.entity.DescriptionImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DescriptionImgRepository extends JpaRepository<DescriptionImage, Long> {
    List<DescriptionImage> findAllByDescriptionImgIdIn(List<Long> descriptionImgIds);

}
