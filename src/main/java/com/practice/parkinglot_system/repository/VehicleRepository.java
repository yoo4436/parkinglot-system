package com.practice.parkinglot_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.practice.parkinglot_system.entity.VehicleEntity;

public interface VehicleRepository extends JpaRepository<VehicleEntity, String> {
    List<VehicleEntity> findByOwner_Username(String username);
} 
