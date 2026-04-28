package com.practice.parkinglot_system.repository;

import com.practice.parkinglot_system.entity.ParkingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ParkingRecordRepository extends JpaRepository<ParkingRecord, Long> {
    
    // Spring Data JPA 會根據方法名稱自動產生 SQL
    // 尋找「車牌號碼」且「狀態為停車中」的那一筆紀錄
    Optional<ParkingRecord> findByPlateNumberAndStatus(String plateNumber, String status);
}
