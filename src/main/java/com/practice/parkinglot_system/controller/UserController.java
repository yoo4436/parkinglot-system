package com.practice.parkinglot_system.controller;

import com.practice.parkinglot_system.entity.ParkingRecord;
import com.practice.parkinglot_system.entity.VehicleEntity;
import com.practice.parkinglot_system.repository.ParkingRecordRepository;
import com.practice.parkinglot_system.repository.VehicleRepository;
import com.practice.parkinglot_system.service.ParkingLot;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final VehicleRepository vehicleRepository;
    private final ParkingRecordRepository recordRepository;
    private final ParkingLot parkingLot;

    public UserController(VehicleRepository vehicleRepository, ParkingRecordRepository recordRepository, ParkingLot parkingLot) {
        this.vehicleRepository = vehicleRepository;
        this.recordRepository = recordRepository;
        this.parkingLot = parkingLot;
    }

    // 取得當前登入車主的所有車輛狀態
    @GetMapping("/vehicles")
    public ResponseEntity<?> getMyVehicles(Principal principal) {
        // 1. principal.getName() 會自動拿出 JWT 裡面的帳號 (例如 "admin")
        String username = principal.getName();
        
        // 2. 去資料庫撈出這個帳號名下的所有車輛
        List<VehicleEntity> vehicles = vehicleRepository.findByOwner_Username(username);

        // 3. 把資料整理成前端儀表板需要的格式 (List 包 Map)
        List<Map<String, Object>> resultList = new ArrayList<>();
        
        for (VehicleEntity v : vehicles) {
            Map<String, Object> map = new HashMap<>();
            map.put("plate", v.getPlateNumber());
            map.put("model", v.getVehicleType().equals("CAR") ? "汽車" : "機車"); // 簡單把型態轉成中文
            map.put("type", v.getVehicleType());
            
            // 檢查這台車目前有沒有「停車中」的紀錄
            Optional<ParkingRecord> recordOpt = recordRepository.findByVehicle_PlateNumberAndStatus(v.getPlateNumber(), "PARKING");
            
            if (recordOpt.isPresent()) {
                ParkingRecord record = recordOpt.get();
                map.put("status", "PARKING");
                // 將時間格式轉成字串，並把中間的 'T' 換成空白，讓前端更好顯示
                map.put("entryTime", record.getEntryTime().toString().replace("T", " ")); 
                // 完美重複利用我們之前寫好的計費邏輯
                map.put("currentFee", parkingLot.getFee(v.getPlateNumber())); 
            } else {
                map.put("status", "HOME");
                map.put("entryTime", null);
                map.put("currentFee", 0);
            }
            resultList.add(map);
        }

        return ResponseEntity.ok(resultList);
    }
}