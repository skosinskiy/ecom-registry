package com.kosinskyi.ecom.registry.repository;

import com.kosinskyi.ecom.registry.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

  Optional<User> findByJwtRefreshToken(String jwtRefreshToken);

}
