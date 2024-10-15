package com.hoxy.hoxymall.service;

import com.hoxy.hoxymall.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ProductRepository productRepository;
    private final String uploadDir = "C:\\Users\\Seo Young Ho\\Desktop\\hoxymall\\src\\main\\resources\\static\\images";


    public String uploadImg(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어있습니다");
        }

        try {
            //파일 이름 생성
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path path = Paths.get(uploadDir + File.separator + fileName);

            //파일 저장
            Files.copy(file.getInputStream(), path);

            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("파일 저장에 실패하였습니다.", e);
        }
    }
}
