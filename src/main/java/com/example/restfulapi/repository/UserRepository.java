package com.example.restfulapi.repository;

import com.example.restfulapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);

    Optional<User> findFirstByUsername(String username);

    Optional<User> findFirstByToken(String token);
}
