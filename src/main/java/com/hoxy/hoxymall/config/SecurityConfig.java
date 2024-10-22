package com.hoxy.hoxymall.config;
import com.hoxy.hoxymall.oauth.PrincipalOauth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@PropertySource("classpath:application-secret.properties")

public class SecurityConfig {

    private final PrincipalOauth2UserService userService;

    // 생성자 주입
    public SecurityConfig(@Lazy PrincipalOauth2UserService principalOauth2UserService) {
        this.userService = principalOauth2UserService;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .headers(headersConfigurer -> headersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/users/new").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/users/**").hasRole("USER")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .defaultSuccessUrl("/", true)
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .loginPage("/users/login")
                        .failureUrl("/users/login?error=true") // 일반 로그인 실패 시 URL
                        .permitAll())

                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/users/login")
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                                .userService(userService)) // 사용자 정의 userService 설정
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/users/login?oauth_error=true") // 소셜 로그인 실패 시 URL
                );

        return http.build();
    }

}
