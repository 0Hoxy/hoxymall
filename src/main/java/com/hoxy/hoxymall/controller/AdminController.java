package com.hoxy.hoxymall.controller;

import com.hoxy.hoxymall.dto.AllUserDTO;
import com.hoxy.hoxymall.dto.GetUserDTO;
import com.hoxy.hoxymall.dto.NewUserDTO;
import com.hoxy.hoxymall.entity.User;
import com.hoxy.hoxymall.repository.UserRepository;
import com.hoxy.hoxymall.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final UserRepository userRepository;


    @PostMapping("new")
    @ResponseBody
    public ResponseEntity<NewUserDTO> newAdmin(@RequestBody NewUserDTO newUserDTO) {
        NewUserDTO createdAdmin = userService.newAdmin(newUserDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAdmin);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<NewUserDTO> getAdmin(@RequestBody NewUserDTO newUserDTO, @PathVariable Long id) {
        NewUserDTO adminUser = userService.updateAdmin(id, newUserDTO);
        return ResponseEntity.ok(adminUser);
    }

    @GetMapping("/data/{id}")
    public String updateAdmin(@PathVariable Long id, Model model) {
        NewUserDTO adminUser = userService.getUpdateAdminId(id);
        model.addAttribute("user", adminUser);
        return "updateAdmin";
    }
}
