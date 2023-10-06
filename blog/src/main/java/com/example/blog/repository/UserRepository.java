package com.example.blog.repository;

import com.example.blog.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email); // 이걸 왜 굳이 또했지...? 굳이 안해도 되지 않나..? findById가 아니라 그런듯?
}
