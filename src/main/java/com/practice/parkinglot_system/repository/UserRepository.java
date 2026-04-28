package com.practice.parkinglot_system.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.practice.parkinglot_system.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    // 透過帳號查詢會員
    Optional<UserEntity> findByUsername(String username);
}
