package com.hoxy.hoxymall.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;
//    @Column(nullable = false)
    private String email;
    private String name;
    private String phoneNumber;
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING) // Enum 타입을 문자열로 저장
    private Set<Role> roles = new HashSet<>();

    private String provider;
    private String providerId;


    public User(String username, String password, String email, String name, Role role, String provider, String providerId) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.roles = Set.of(role);
        this.provider = provider;
        this.providerId = providerId;
        this.createdAt = LocalDateTime.now();
    }


}
