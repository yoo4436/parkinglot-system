package com.practice.parkinglot_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "parking_records")
@Getter
@Setter
public class ParkingRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 多對一關聯。多筆紀錄對應到同一台車
    @ManyToOne
    @JoinColumn(name = "vehicle_plate", nullable = false)
    private VehicleEntity vehicle;

    @Column(nullable = false)
    private LocalDateTime entryTime;

    private LocalDateTime exitTime;

    private Integer fee;

    @Column(nullable = false)
    private String status;

    public ParkingRecord() {}

    public ParkingRecord(VehicleEntity vehicle) {
        this.vehicle = vehicle;
        this.entryTime = LocalDateTime.now().withNano(0);
        this.status = "PARKING";
    }
}