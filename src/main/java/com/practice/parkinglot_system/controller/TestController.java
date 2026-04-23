package com.practice.parkinglot_system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // 告訴 Spring 說：「嗨！我是一個負責接客的 API 櫃台」
public class TestController {

    @GetMapping("/hello") // 當前端瀏覽器來到 "/hello" 這個網址時，請執行下面這個方法
    public String sayHello() {
        return "Spring Boot 停車場伺服器已經啟動了！";
    }
}
