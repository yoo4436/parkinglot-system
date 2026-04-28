package com.practice.parkinglot_system.controller;

import com.practice.parkinglot_system.entity.UserEntity;
import com.practice.parkinglot_system.repository.UserRepository;
import com.practice.parkinglot_system.util.JwtToken;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final JwtToken jwtToken;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, JwtToken jwtToken, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtToken = jwtToken;
        this.passwordEncoder = passwordEncoder;
    }

    // 1. 快速註冊帳號 API (方便我們測試)
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestParam String username, @RequestParam String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            return ResponseEntity.badRequest().body("帳號已被使用！");
        }
        
        // 將密碼加密後存入資料庫
        UserEntity newUser = new UserEntity(username, passwordEncoder.encode(password));
        userRepository.save(newUser);
        
        return ResponseEntity.ok("註冊成功！");
    }

    // 2. 登入並獲取 JWT API
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
        Optional<UserEntity> userOpt = userRepository.findByUsername(username);
        
        // 檢查帳號是否存在，並且比對加密後的密碼是否正確
        if (userOpt.isPresent() && passwordEncoder.matches(password, userOpt.get().getPassword())) {
            
            // 驗證成功，核發 JWT 通行證
            String token = jwtToken.generateToken(username);
            
            // 將 Token 包裝成 JSON 格式回傳給 React 前端
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return ResponseEntity.ok(response);
        }
        
        return ResponseEntity.status(401).body("帳號或密碼錯誤");
    }
}