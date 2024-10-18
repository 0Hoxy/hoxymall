package com.hoxy.hoxymall.service;

import com.hoxy.hoxymall.dto.AllUserDTO;
import com.hoxy.hoxymall.dto.GetUserDTO;
import com.hoxy.hoxymall.dto.NewUserDTO;
import com.hoxy.hoxymall.entity.Role;
import com.hoxy.hoxymall.entity.User;
import com.hoxy.hoxymall.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    public NewUserDTO updateUser(NewUserDTO newUserDTO, Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("해당 아이디는 존재하지 않습니다"));
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

}
