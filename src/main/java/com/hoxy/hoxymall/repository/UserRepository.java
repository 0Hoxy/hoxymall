package com.hoxy.hoxymall.repository;

import com.hoxy.hoxymall.entity.Role;
import com.hoxy.hoxymall.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findByEmail(String email);

    User findByProviderId(String providerId);



}
