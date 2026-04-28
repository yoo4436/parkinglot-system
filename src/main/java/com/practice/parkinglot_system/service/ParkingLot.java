package com.practice.parkinglot_system.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.practice.parkinglot_system.entity.Car;
import com.practice.parkinglot_system.entity.Motorcycle;
import com.practice.parkinglot_system.entity.ParkingRecord;
import com.practice.parkinglot_system.entity.Vehicle;
import com.practice.parkinglot_system.entity.VehicleEntity;
import com.practice.parkinglot_system.repository.ParkingRecordRepository;
import com.practice.parkinglot_system.repository.VehicleRepository;

@Service
public class ParkingLot {
    private final ParkingRecordRepository recordRepo;
    private final VehicleRepository vehicleRepo;

    public ParkingLot(ParkingRecordRepository recordRepo, VehicleRepository vehicleRepo) {
        this.recordRepo = recordRepo;
        this.vehicleRepo = vehicleRepo;
    }

    public void checkIn(Vehicle vehicle) {
        // 1. 處理車輛主檔：如果資料庫還沒有這台車，就存入
        VehicleEntity vehicleEntity = vehicleRepo.findById(vehicle.getPlateNumber())
            .orElseGet(() -> {
                String type = (vehicle instanceof Car) ? "CAR" : "MOTORCYCLE";
                return vehicleRepo.save(new VehicleEntity(vehicle.getPlateNumber(), type));
            });

        // 2. 建立停車紀錄
        ParkingRecord record = new ParkingRecord(vehicleEntity);
        recordRepo.save(record);
    }

    public int getFee(String plateNumber) {
        // 透過新方法查詢「停車中」的紀錄
        Optional<ParkingRecord> recordOpt = recordRepo.findByVehicle_PlateNumberAndStatus(plateNumber, "PARKING");
        
        if (recordOpt.isEmpty()) return -1;

        ParkingRecord record = recordOpt.get();
        VehicleEntity vEntity = record.getVehicle(); // 從紀錄拿回車輛資訊

        LocalDateTime entryTime = record.getEntryTime();
        LocalDateTime now = LocalDateTime.now();
        long minutes = Duration.between(entryTime, now).toMinutes();

        int halfHour = (int) Math.ceil(minutes / 30.0);
        int hours = (int) Math.ceil(minutes / 60.0);

        if (hours == 0) hours = 1; // 不足一小時按一小時計費

        // 具現化多型邏輯
        Vehicle v = vEntity.getVehicleType().equals("CAR") ? new Car(plateNumber) : new Motorcycle(plateNumber);
        return v.calculateFee(hours); 
    }

    public LocalDateTime getEntryTime(String plateNumber) {
        // 3：使用 Optional 來接收查詢結果，這是 Java 8 後防範 NullPointerException 的好習慣
        Optional<ParkingRecord> recordOpt = recordRepo.findByVehicle_PlateNumberAndStatus(plateNumber, "PARKING");
        
        if (recordOpt.isEmpty()) {
            System.out.println("查無資料");
            return null;
        }
        
        LocalDateTime entryTime = recordOpt.get().getEntryTime();
        System.out.printf("車牌 %s 入場時間為 %s\n", plateNumber, entryTime);
        return entryTime;
    }

    public void checkOut(String plateNumber) {
        Optional<ParkingRecord> recordOpt = recordRepo.findByVehicle_PlateNumberAndStatus(plateNumber, "PARKING");

        if (recordOpt.isEmpty()) {
            System.out.println("查無資料");
            return;
        }

        // 取出這筆紀錄
        ParkingRecord record = recordOpt.get();
        
        // 呼叫下方我們自己寫的 getFee 來算錢
        int parkingFee = getFee(plateNumber); 
        System.out.printf("車牌 %s 停車費用為 %d 元，已結帳離場\n", plateNumber, parkingFee);
        
        // 4：結帳時「不刪除」資料，而是更新出場時間、費用與狀態
        record.setExitTime(LocalDateTime.now().withNano(0));
        record.setFee(parkingFee);
        record.setStatus("COMPLETED");
        
        // 將更新後的紀錄存回資料庫
        recordRepo.save(record);
    }
}