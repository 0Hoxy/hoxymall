package com.hoxy.hoxymall.controller;

import com.hoxy.hoxymall.dto.NewUserDTO;
import com.hoxy.hoxymall.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    @PostMapping("new")
    public ResponseEntity<NewUserDTO> newAdmin(@RequestBody NewUserDTO newUserDTO) {
        NewUserDTO createdAdmin = userService.newAdmin(newUserDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAdmin);
    }

}
