package com.practice.parkinglot_system.controller;

import org.springframework.web.bind.annotation.RestController;

import com.practice.parkinglot_system.entity.Car;
import com.practice.parkinglot_system.entity.Motorcycle;
import com.practice.parkinglot_system.entity.Vehicle;
import com.practice.parkinglot_system.service.ParkingLot;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/parking")
@CrossOrigin(origins = "*")
public class ParkingController {
    private final ParkingLot parkingLot;

    public ParkingController(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }
    
    @PostMapping("/checkIn")
    public String checkInVehicle(@RequestParam String plateNumber, @RequestParam String type) {
        Vehicle vehicle = null;

        if ("CAR".equalsIgnoreCase(type)) {
            vehicle = new Car(plateNumber);
        } else if ("MOTORCYCLE".equalsIgnoreCase(type)) {
            vehicle = new Motorcycle(plateNumber);
        } else {
            return "進場失敗：不支援的車輛類型";
        }
        
        parkingLot.checkIn(vehicle);
        return "車牌 " + plateNumber + " 已入場";
    }
    
    @GetMapping("/entryTime")
    public String getEntryTime(@RequestParam String plateNumber) {
        LocalDateTime entryTime = parkingLot.getEntryTime(plateNumber);
        if (entryTime == null) {
            return "查無資料";
        } else {
            return "車牌 " + plateNumber + " 入場時間為 " + entryTime;
        }
    }

    @PostMapping("/checkOut")
    public String checkOutVehicle(@RequestParam String plateNumber) {
        Vehicle vehicle = null;
        
        parkingLot.checkOut(plateNumber);
        return "車牌 " + plateNumber + " 已離場";
    }
    
    // 查詢費用的 API
    @GetMapping("/fee")
    public String getParkingFee(@RequestParam String plateNumber) {
        int fee = parkingLot.getFee(plateNumber);
        
        if (fee == -1) {
            return "查無車輛";
        }
        
        // 如果有找到車，就把算出來的金額（數字）轉成純文字字串回傳
        return String.valueOf(fee); 
    }
    
}
