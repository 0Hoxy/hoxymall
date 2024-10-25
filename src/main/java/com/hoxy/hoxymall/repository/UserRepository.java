package com.hoxy.hoxymall.repository;

import com.hoxy.hoxymall.entity.Role;
import com.hoxy.hoxymall.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findByEmail(String email);

    User findByProviderId(String providerId);

    List<User> findByRoles(Role role);

    Optional<User> findByUserIdAndRoles(Long id, Role role); //정확한 필드명 입력해야한다.
}
