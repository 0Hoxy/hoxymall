package com.hoxy.hoxymall.service;

import com.hoxy.hoxymall.entity.User;
import com.hoxy.hoxymall.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

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
        return new org.springframework.security.core.userdetails.User( //userDetails import
                user.getUsername(),
                user.getPassword(),
                new ArrayList<>() //권한 설정(필요 시 추가)
        );
    }
}
