package com.hoxy.hoxymall.config;

import com.hoxy.hoxymall.oauth.PrincipalOauth2UserService;
import com.hoxy.hoxymall.service.UserService;
import lombok.RequiredArgsConstructor;
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

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
@PropertySource("classpath:application-secret.properties")
public class SecurityConfig {

    private final PrincipalOauth2UserService userService;

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
                                .requestMatchers("/users/**").permitAll()
//                        .requestMatchers("/products/**").hasRole("ADMIN")
//                        .requestMatchers("/categories/**").hasRole("ADMIN")
                                .requestMatchers(antMatcher("/admin/**")).hasRole("ADMIN")
                                .requestMatchers(antMatcher("/user/**")).hasRole("USER")
                                .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .defaultSuccessUrl("/products", true)
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .loginPage("/users/login")
                        .failureUrl("/users/login")
                        .permitAll())

                .oauth2Login(oauth2configurer -> oauth2configurer
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                                .userService(userService)));
        return http.build();
    }
}
