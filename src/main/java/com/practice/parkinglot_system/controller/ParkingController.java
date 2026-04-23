package com.practice.parkinglot_system.controller;

import org.springframework.web.bind.annotation.RestController;

import com.practice.parkinglot_system.service.ParkingLot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/parking")
public class ParkingController {
    private final ParkingLot parkingLot;

    public ParkingController(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }
    
    @PostMapping("/checkIn")
    public String checkInVehicle(@RequestParam String plateNumber, @RequestParam String type) {
        //TODO: process POST request
        
        return null;
    }
    
    
}
