package com.practice.parkinglot_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
public class VehicleEntity {

    @Id
    private String plateNumber; // 直接用車牌當主鍵，符合唯一性

    @Column(nullable = false)
    private String vehicleType; // CAR / MOTORCYCLE

    @ManyToOne
    @JoinColumn(name = "user_id") // 在 vehicles 表裡會多出一個 user_id 欄位
    private UserEntity owner;

    // 一台車可以有多筆停車紀錄
    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
    private List<ParkingRecord> records;

    public VehicleEntity() {}

    public VehicleEntity(String plateNumber, String vehicleType) {
        this.plateNumber = plateNumber;
        this.vehicleType = vehicleType;
    }
}
