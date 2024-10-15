package com.hoxy.hoxymall.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    //ResourceHandlerRegistry를 사용해 파일 시스템 경로에 매핑함(정적 리소스 처리 최적화, 로딩 속도 굉장히 빨라졌음)
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/images/**")
                .addResourceLocations("file:/C:/Users/Seo Young Ho/Desktop/hoxymall/src/main/resources/static/images/");
    }
}
