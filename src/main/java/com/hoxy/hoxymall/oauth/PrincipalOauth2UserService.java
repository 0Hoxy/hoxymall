package com.hoxy.hoxymall.oauth;

import com.hoxy.hoxymall.entity.Role;
import com.hoxy.hoxymall.entity.User;
import com.hoxy.hoxymall.exception.DuplicateEmailException;
import com.hoxy.hoxymall.oauth.info.GoogleUserInfo;
import com.hoxy.hoxymall.oauth.info.KakaoUserInfo;
import com.hoxy.hoxymall.oauth.info.NaverUserInfo;
import com.hoxy.hoxymall.oauth.info.OAuth2UserInfo;
import com.hoxy.hoxymall.repository.UserRepository;
import com.hoxy.hoxymall.util.SkuGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(PrincipalOauth2UserService.class);

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        OAuth2UserInfo oAuth2UserInfo;
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        try {
            switch (registrationId) {
                case "google" -> oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
                case "kakao" -> oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
                case "naver" ->
                        oAuth2UserInfo = new NaverUserInfo((Map<String, Object>) oAuth2User.getAttributes().get("response"));
                default -> {
                    logger.warn("지원하지 않는 로그인 방식입니다: {}", registrationId);
                    throw new OAuth2AuthenticationException("지원하지 않는 로그인 방식입니다.");
                }
            }

            if (oAuth2UserInfo == null) {
                throw new OAuth2AuthenticationException("OAuth2UserInfo를 생성하는 데 실패했습니다.");
            }

            String rawPassword = SkuGenerator.generateInitialPassword(12);
            String provider = oAuth2UserInfo.getProvider();
            String providerId = oAuth2UserInfo.getProviderId();
            String email = oAuth2UserInfo.getProviderEmail();
            String username = provider + "_" + providerId;
            String name = oAuth2UserInfo.getProviderName();
            String password = passwordEncoder.encode(rawPassword);
            Role role = Role.ROLE_USER;

            // 이메일 중복 검증
            User existingUserByEmail = userRepository.findByEmail(email);
            if (existingUserByEmail != null) {
                // 이메일이 이미 존재하는 경우
                if (existingUserByEmail.getProvider() != null && !existingUserByEmail.getProvider().equals(provider)) {
                    logger.info("이미 다른 소셜 로그인 방식으로 가입된 이메일입니다: {}", existingUserByEmail.getProvider());
                    throw new DuplicateEmailException("이미 " + existingUserByEmail.getProvider() + "로 가입된 이메일입니다.");
                } else if (existingUserByEmail.getProvider() == null) {
                    existingUserByEmail.setProvider(provider);
                    existingUserByEmail.setProviderId(providerId);
                    logger.info("일반가입 유저 업데이트: {} 계정연결", provider);
                    return new PrincipalDetails(existingUserByEmail, oAuth2User.getAttributes());
                } else {
                    logger.info("이미 로그인한 사용자: {}", existingUserByEmail.getUsername());
                    return new PrincipalDetails(existingUserByEmail, oAuth2User.getAttributes());
                }
            }

            // 사용자 중복 검증
            User user = userRepository.findByProviderId(providerId);
            if (user == null) {
                // 신규 사용자 등록
                user = new User(username, password, email, name, role, provider, providerId);
                userRepository.save(user);
                logger.info("신규 사용자 등록: {}", username);
                logger.info("이메일: {}", email);

            }  else {
                logger.info("이미 로그인을 한 사용자: {}", username);
            }

            return new PrincipalDetails(user, oAuth2User.getAttributes());
        } catch (Exception e) {
            logger.error("사용자 정보를 로드하는 중 오류 발생: {}", e.getMessage());
            throw new OAuth2AuthenticationException("사용자 정보를 로드하는 중 오류 발생: " + e.getMessage());
        }
    }
}