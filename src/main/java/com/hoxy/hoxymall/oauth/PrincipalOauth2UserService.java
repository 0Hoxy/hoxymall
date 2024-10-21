package com.hoxy.hoxymall.oauth;

import com.hoxy.hoxymall.entity.Role;
import com.hoxy.hoxymall.entity.User;
import com.hoxy.hoxymall.oauth.info.GoogleUserInfo;
import com.hoxy.hoxymall.oauth.info.KakaoUserInfo;
import com.hoxy.hoxymall.oauth.info.NaverUserInfo;
import com.hoxy.hoxymall.oauth.info.OAuth2UserInfo;
import com.hoxy.hoxymall.repository.UserRepository;
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
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(PrincipalOauth2UserService.class);
    private static final String DEFAULT_PASSWORD = "1234"; // 상수로 비밀번호 선언

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        OAuth2UserInfo oAuth2UserInfo = null;
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        try {
            if (registrationId.equals("google")) {
                oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
            } else if (registrationId.equals("kakao")) {
                oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
            } else if (registrationId.equals("naver")) {
                oAuth2UserInfo = new NaverUserInfo((Map<String, Object>) oAuth2User.getAttributes().get("response"));
            } else {
                logger.warn("지원하지 않는 로그인 방식입니다: {}", registrationId);
                throw new OAuth2AuthenticationException("지원하지 않는 로그인 방식입니다.");
            }

            if (oAuth2UserInfo == null) {
                throw new OAuth2AuthenticationException("OAuth2UserInfo를 생성하는 데 실패했습니다.");
            }

            String provider = oAuth2UserInfo.getProvider();
            String providerId = oAuth2UserInfo.getProviderId();
            String email = oAuth2UserInfo.getProviderEmail();
            String username = provider + "_" + providerId;
            String name = oAuth2UserInfo.getProviderName();
            String password = passwordEncoder.encode(DEFAULT_PASSWORD);
            Role role = Role.ROLE_USER;

            User user = userRepository.findByUsername(username);
            if (user == null) {
                user = new User(username, password, email, name, role, provider, providerId);
                userRepository.save(user);
                logger.info("신규 사용자 등록: {}", username);
            } else {
                logger.info("이미 로그인을 한 사용자: {}", username);
            }

            return new PrincipalDetails(user, oAuth2User.getAttributes());
        } catch (Exception e) {
            logger.error("사용자 정보를 로드하는 중 오류 발생: {}", e.getMessage());
            throw new OAuth2AuthenticationException("사용자 정보를 로드하는 중 오류 발생");
        }
    }
}
