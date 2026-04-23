package com.practice.parkinglot_system.entity;

public class Motorcycle extends Vehicle {
    public Motorcycle(String plateNumber) {
        super(plateNumber);
    }

    @Override
    public int calculateFee(int hours) {
        return hours * 20; // Example fee calculation for motorcycles
    }

}
