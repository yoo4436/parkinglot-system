package com.practice.parkinglot_system.repository;

import com.practice.parkinglot_system.entity.ParkingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ParkingRecordRepository extends JpaRepository<ParkingRecord, Long> {
    // 改為透過關聯物件的車牌來搜尋
    Optional<ParkingRecord> findByVehicle_PlateNumberAndStatus(String plateNumber, String status);
}
