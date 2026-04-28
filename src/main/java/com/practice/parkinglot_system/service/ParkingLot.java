package com.practice.parkinglot_system.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.practice.parkinglot_system.entity.Vehicle;

@Service
public class ParkingLot {
    // private Map<String, LocalDateTime> parkingRecords = new HashMap<>();
    private Map<String, Vehicle> parkingRecords = new HashMap<>();

    // public void checkIn(String plateNumber) {
    //     LocalDateTime now = LocalDateTime.now();

    //     parkingRecords.put(plateNumber, now);

    //     System.out.printf("車牌 %s 已於 %s 入場\n", plateNumber, now);
    // }

    public void checkIn(Vehicle vehicle) {
        parkingRecords.put(vehicle.getPlateNumber(), vehicle);
        //除錯用
        System.out.printf("車牌 %s (%s) 已入場\n", vehicle.getPlateNumber(), vehicle.getClass().getSimpleName());
    }

    public LocalDateTime getEntryTime(String plateNumber) {
        Vehicle vehicle = parkingRecords.get(plateNumber);
        LocalDateTime entryTime = vehicle.getEntryTime();
        if (entryTime == null) {
            System.out.println("查無資料");
        } else {
            System.out.printf("車牌 %s 入場時間為 %s\n", plateNumber, entryTime);
        }

        return entryTime;
    }

    public void checkOut(String plateNumber) {
        Vehicle vehicle = parkingRecords.get(plateNumber);

        if (vehicle == null) {
            System.out.println("查無資料");
            return;
        }

        int hours = 2;

        int parkingFee = vehicle.calculateFee(hours);

        System.out.printf("車牌 %s 停車費用為 %d 元\n", plateNumber, parkingFee);
        parkingRecords.remove(vehicle.getPlateNumber());
    }

    // 單純計算費用，不把車輛從 parkingRecords 中移除
    public int getFee(String plateNumber) {
        Vehicle vehicle = parkingRecords.get(plateNumber);
        
        // 防呆：如果查無此車，回傳 -1 做為錯誤代碼
        if (vehicle == null) {
            return -1; 
        }

        int hours = 2; // 目前為了測試方便，我們先寫死模擬停了 2 小時
        return vehicle.calculateFee(hours); // 這裡依然完美利用了多型！
    }
}
