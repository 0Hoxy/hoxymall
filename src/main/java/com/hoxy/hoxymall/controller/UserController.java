package com.hoxy.hoxymall.controller;

import com.hoxy.hoxymall.dto.AllUserDTO;
import com.hoxy.hoxymall.dto.GetUserDTO;
import com.hoxy.hoxymall.dto.NewUserDTO;
import com.hoxy.hoxymall.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;


    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/new")
    @ResponseBody
    public ResponseEntity<NewUserDTO> newUser(@RequestBody NewUserDTO newUserDTO) {
        NewUserDTO createdUser = userService.newUser(newUserDTO);
        System.out.println(createdUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<NewUserDTO> updateUser(@RequestBody NewUserDTO newUserDTO, @PathVariable Long id) {
        NewUserDTO updatedUser = userService.updateUser(newUserDTO, id);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<NewUserDTO> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    @ResponseBody
    public ResponseEntity<List<AllUserDTO>> getUser() {
        List<AllUserDTO> allUsers = userService.getAllUsers();
        return ResponseEntity.ok(allUsers);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<GetUserDTO> getUser(@PathVariable Long id) {
        GetUserDTO getUser = userService.getUser(id);
        return ResponseEntity.ok(getUser);
    }
}
