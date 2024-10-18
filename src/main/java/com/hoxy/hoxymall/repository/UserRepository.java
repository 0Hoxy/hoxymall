package com.hoxy.hoxymall.repository;

import com.hoxy.hoxymall.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
