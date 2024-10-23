package com.hoxy.hoxymall.service;

import com.hoxy.hoxymall.dto.AllUserDTO;
import com.hoxy.hoxymall.dto.GetUserDTO;
import com.hoxy.hoxymall.dto.NewUserDTO;
import com.hoxy.hoxymall.entity.Role;
import com.hoxy.hoxymall.entity.User;
import com.hoxy.hoxymall.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public NewUserDTO newUser(NewUserDTO newUserDTO) {
        User user = modelMapper.map(newUserDTO, User.class);
        user.setPassword(passwordEncoder.encode(newUserDTO.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setRoles(Set.of(Role.ROLE_USER));
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, NewUserDTO.class);
    }

    public NewUserDTO newAdmin(NewUserDTO newUserDTO) {
        User user = modelMapper.map(newUserDTO, User.class);
        user.setPassword(passwordEncoder.encode(newUserDTO.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setRoles(Set.of(Role.ROLE_ADMIN));
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, NewUserDTO.class);

    }

    public NewUserDTO updateUser(NewUserDTO newUserDTO, Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("해당 아이디는 존재하지 않습니다"));
        user.setName((newUserDTO.getName()));
        user.setEmail(newUserDTO.getEmail());
        user.setAddress(newUserDTO.getAddress());
        user.setPhoneNumber(newUserDTO.getPhoneNumber());
        user.setUpdatedAt(LocalDateTime.now());
        return modelMapper.map(userRepository.save(user), NewUserDTO.class);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public List<AllUserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(user -> modelMapper.map(user, AllUserDTO.class)).collect(Collectors.toList());
    }

    public GetUserDTO getUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("해당 유저는 존재하지 않습니다."));
        return modelMapper.map(user, GetUserDTO.class);
    }

    public NewUserDTO updateAdmin(Long id, NewUserDTO newUserDTO) {
        // ID로 사용자 조회
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("{}번 ID 사용자를 찾을 수 없습니다.", id);
                    return new EntityNotFoundException("사용자를 찾을 수 없습니다.");
                });

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            log.info("인증된 사용자: {}", authentication.getName());
            log.info("사용자의 권한: {}", authentication.getAuthorities());
            log.info("현재 필요한 권한: {}", authentication.getAuthorities());
        } else {
            log.error("사용자가 인증되지 않았습니다.");
        }

        // ADMIN 권한 확인
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            log.error("권한이 없습니다. 현재 권한은 {} 입니다.", authentication.getAuthorities());
            throw new AccessDeniedException("권한이 없습니다.");
        }

        // 사용자 정보 업데이트
        user.setName(newUserDTO.getName());
        user.setEmail(newUserDTO.getEmail());
        user.setAddress(newUserDTO.getAddress());
        user.setPhoneNumber(newUserDTO.getPhoneNumber());
        user.setUpdatedAt(LocalDateTime.now());

        // DTO로 변환하여 반환
        return modelMapper.map(userRepository.save(user), NewUserDTO.class);
    }



    public NewUserDTO getUpdateAdminId(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("해당 유저는 찾을 수 없다."));
        return modelMapper.map(user, NewUserDTO.class);
    }
}
