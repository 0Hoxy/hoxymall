package com.hoxy.hoxymall.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class NewUserDTO {
    private String username;
    private String password;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private LocalDateTime createdAt;
}
