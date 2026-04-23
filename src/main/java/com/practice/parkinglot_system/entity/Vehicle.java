package com.practice.parkinglot_system.entity;

import java.time.LocalDateTime;

public abstract class Vehicle {
    protected String plateNumber;
    protected LocalDateTime entryTime;

    public Vehicle(String plateNumber) {
        this.plateNumber = plateNumber;
        this.entryTime = LocalDateTime.now();
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public abstract int calculateFee(int hours);
}
