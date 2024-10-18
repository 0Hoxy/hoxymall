package com.hoxy.hoxymall.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                                .requestMatchers("/login").permitAll()
                                .requestMatchers("/users/**").permitAll()
//                        .requestMatchers("/products/**").hasRole("ADMIN")
//                        .requestMatchers("/categories/**").hasRole("ADMIN")
                                .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .defaultSuccessUrl("/products", true)
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .permitAll());
                /*
                사용자 정의 페이지
                 .loginPage("/custom-login") // 사용자 정의 로그인 페이지 경로
                 .permitAll()
                 */

        return http.build();
    }
}
