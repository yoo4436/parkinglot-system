package com.practice.parkinglot_system.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 帳號必須是唯一的
    @Column(nullable = false, unique = true)
    private String username;

    // 密碼 (未來我們會把它加密後再存進來)
    @Column(nullable = false)
    private String password;

    // 關聯：一個會員可以擁有多台車輛
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<VehicleEntity> vehicles;
    
    public UserEntity() {}
    
    public UserEntity(String username, String password) {
        this.username = username;
        this.password = password;
    }
}