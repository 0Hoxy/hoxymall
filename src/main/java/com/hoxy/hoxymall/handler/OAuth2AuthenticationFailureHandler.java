package com.hoxy.hoxymall.handler;

import com.hoxy.hoxymall.exception.DuplicateEmailException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        String errorMessage = "소셜 로그인 중 오류가 발생했습니다.";

        // DuplicateEmailException에서 발생한 메시지 처리
        if (exception.getCause() instanceof DuplicateEmailException) {
            errorMessage = exception.getCause().getMessage();
        }

        // URL 인코딩하여 에러 메시지 전달
        String encodedErrorMessage = URLEncoder.encode(errorMessage, StandardCharsets.UTF_8.toString());
        setDefaultFailureUrl("/auth/login?error=true&exception=" + encodedErrorMessage);

        super.onAuthenticationFailure(request, response, exception);
    }
}