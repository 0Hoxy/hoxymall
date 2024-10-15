package com.hoxy.hoxymall.service;

import com.hoxy.hoxymall.entity.DescriptionImage;
import com.hoxy.hoxymall.entity.ProductImage;
import com.hoxy.hoxymall.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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

    public void deleteImagesFromFileSystem(List<ProductImage> productImages) {
        for (ProductImage image : productImages) {
            File file = new File(uploadDir + File.separator + image.getProductImgUrl()); // 이미지 저장 경로
            System.out.println("삭제 시도: " + file.getAbsolutePath()); // 삭제하려는 파일 경로 출력
            if (file.exists()) {
                try {
                    boolean deleted = file.delete(); // 파일 삭제 시 성공 여부를 반환
                    if (deleted) {
                        System.out.println("파일 삭제 성공: " + file.getAbsolutePath());
                    } else {
                        System.err.println("파일 삭제 실패: " + file.getAbsolutePath());
                    }
                } catch (Exception e) {
                    System.err.println("파일 삭제 중 오류 발생: " + e.getMessage());
                }
            } else {
                System.out.println("파일이 존재하지 않음: " + file.getAbsolutePath());
            }
        }
    }

    public void deleteDescriptionImagesFromFileSystem(List<DescriptionImage> descriptionImages) {
        for (DescriptionImage image : descriptionImages) {
            File file = new File(uploadDir + File.separator + image.getDescriptionImgUrl()); // 이미지 저장 경로
            System.out.println("삭제 시도: " + file.getAbsolutePath()); // 삭제하려는 파일 경로 출력
            if (file.exists()) {
                try {
                    boolean deleted = file.delete(); // 파일 삭제 시 성공 여부를 반환
                    if (deleted) {
                        System.out.println("파일 삭제 성공: " + file.getAbsolutePath());
                    } else {
                        System.err.println("파일 삭제 실패: " + file.getAbsolutePath());
                    }
                } catch (Exception e) {
                    System.err.println("파일 삭제 중 오류 발생: " + e.getMessage());
                }
            } else {
                System.out.println("파일이 존재하지 않음: " + file.getAbsolutePath());
            }
        }
    }


}
