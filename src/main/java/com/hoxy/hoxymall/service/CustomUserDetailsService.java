package com.hoxy.hoxymall.service;

import com.hoxy.hoxymall.entity.User;
import com.hoxy.hoxymall.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("해당 유저가 존재하지 않습니다");
        }

        // 사용자 권한 설정
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.toString())) // Enum의 name() 메서드를 사용하여 권한을 설정
                .collect(Collectors.toList());

        // 권한 정보 로그 출력
        authorities.forEach(authority -> log.info("사용자 권한: {}", authority.getAuthority()));

        return new org.springframework.security.core.userdetails.User( // userDetails import
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}
