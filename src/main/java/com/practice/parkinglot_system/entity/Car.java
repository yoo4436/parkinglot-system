package com.practice.parkinglot_system.entity;

public class Car extends Vehicle{
    public Car(String plateNumber) {
        super(plateNumber);
    }

    @Override
    public int calculateFee(int hours) {
        return hours * 60; // 
    }

}
