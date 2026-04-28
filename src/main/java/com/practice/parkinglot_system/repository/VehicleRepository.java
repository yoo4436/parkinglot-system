package com.practice.parkinglot_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.practice.parkinglot_system.entity.VehicleEntity;

public interface VehicleRepository extends JpaRepository<VehicleEntity, String> {
    
} 
