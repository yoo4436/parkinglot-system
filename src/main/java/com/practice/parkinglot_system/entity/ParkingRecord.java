package com.practice.parkinglot_system.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity // 告訴 JPA：這是一個要對應到資料庫的類別
@Table(name = "parking_records") // 指定資料表名稱
@Data
public class ParkingRecord {

    @Id // 主鍵
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自動遞增 ID
    private Long id;

    @Column(nullable = false)
    private String plateNumber; // 車牌

    @Column(nullable = false)
    private String vehicleType; // 車種 (CAR / MOTORCYCLE)

    @Column(nullable = false)
    private LocalDateTime entryTime; // 進場時間

    private LocalDateTime exitTime; // 出場時間 (進場時為空)

    private Integer fee; // 總費用

    @Column(nullable = false)
    private String status; // 狀態 (PARKING / COMPLETED)

    // --- 建構子 ---
    public ParkingRecord() {} // JPA 規定一定要有無參數建構子

    public ParkingRecord(String plateNumber, String vehicleType) {
        this.plateNumber = plateNumber;
        this.vehicleType = vehicleType;
        this.entryTime = LocalDateTime.now().withNano(0);
        this.status = "PARKING";
    }
}