package com.practice.parkinglot_system.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.practice.parkinglot_system.entity.Car;
import com.practice.parkinglot_system.entity.Motorcycle;
import com.practice.parkinglot_system.entity.ParkingRecord;
import com.practice.parkinglot_system.entity.Vehicle;
import com.practice.parkinglot_system.repository.ParkingRecordRepository;

@Service
public class ParkingLot {
    
    // 1：移除 HashMap，改為宣告我們寫好的 Repository
    private final ParkingRecordRepository repository;

    // 透過建構子，讓 Spring Boot 自動幫我們注入資料庫操作工具
    public ParkingLot(ParkingRecordRepository repository) {
        this.repository = repository;
    }

    public void checkIn(Vehicle vehicle) {
        // 2：判斷前端傳來的是汽車還是機車，並轉換成要存入資料庫的字串
        String type = (vehicle instanceof Car) ? "CAR" : "MOTORCYCLE";
        
        // 建立一筆新的資料庫實體 (建構子會自動填入當下時間與 PARKING 狀態)
        ParkingRecord record = new ParkingRecord(vehicle.getPlateNumber(), type);
        
        // 呼叫 JPA 存入資料庫！ (取代原本的 hashMap.put)
        repository.save(record);
        
        System.out.printf("車牌 %s (%s) 已進場並寫入資料庫\n", vehicle.getPlateNumber(), type);
    }

    public LocalDateTime getEntryTime(String plateNumber) {
        // 3：使用 Optional 來接收查詢結果，這是 Java 8 後防範 NullPointerException 的好習慣
        Optional<ParkingRecord> recordOpt = repository.findByPlateNumberAndStatus(plateNumber, "PARKING");
        
        if (recordOpt.isEmpty()) {
            System.out.println("查無資料");
            return null;
        }
        
        LocalDateTime entryTime = recordOpt.get().getEntryTime();
        System.out.printf("車牌 %s 入場時間為 %s\n", plateNumber, entryTime);
        return entryTime;
    }

    public void checkOut(String plateNumber) {
        Optional<ParkingRecord> recordOpt = repository.findByPlateNumberAndStatus(plateNumber, "PARKING");

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
        record.setExitTime(LocalDateTime.now());
        record.setFee(parkingFee);
        record.setStatus("COMPLETED");
        
        // 將更新後的紀錄存回資料庫
        repository.save(record);
    }

    public int getFee(String plateNumber) {
        Optional<ParkingRecord> recordOpt = repository.findByPlateNumberAndStatus(plateNumber, "PARKING");
        
        if (recordOpt.isEmpty()) {
            return -1; 
        }

        ParkingRecord record = recordOpt.get();
        int hours = 2; // 目前為了測試方便，我們先寫死模擬停了 2 小時

        // 5：最精彩的地方！我們把資料庫裡的字串拿出來，重新「具現化」成物件
        // 這樣我們就能完美繼續使用你在 Vehicle, Car, Motorcycle 寫好的「多型計費邏輯」！
        Vehicle vehicle;
        if ("CAR".equals(record.getVehicleType())) {
            vehicle = new Car(plateNumber);
        } else {
            vehicle = new Motorcycle(plateNumber);
        }

        // 呼叫物件自己的計費方法
        return vehicle.calculateFee(hours); 
    }
}