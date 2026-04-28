package com.practice.parkinglot_system.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtToken {

    // ✨ 升級點 1：定義一組超過 32 字元的超長安全密鑰
    private static final String SECRET_KEY_STRING = "ParkingLotSystemSuperSecretKeyForJwtAuthentication2026";
    
    // ✨ 升級點 2：使用 Keys.hmacShaKeyFor 將字串轉換為新版要求的高強度 Key 物件
    private final Key SECRET_KEY = Keys.hmacShaKeyFor(SECRET_KEY_STRING.getBytes(StandardCharsets.UTF_8));
    
    // 設定 Token 有效期為 24 小時 (單位：毫秒)
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24;

    // 從 Token 中提取帳號 (Username)
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 提取 Token 的過期時間
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // 泛型方法：用來提取 Token 中的各種資料 (Claims)
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // 解析整張 Token，如果密鑰不對或是 Token 被篡改，這裡會直接報錯
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 檢查 Token 是否過期
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // 產生一張全新的 Token (發給登入成功的車主)
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username) // 主旨就是使用者的帳號
                .setIssuedAt(new Date(System.currentTimeMillis())) // 簽發時間
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 過期時間
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256) // 用我們的專屬密鑰與 HS256 演算法簽名
                .compact(); // 壓縮成字串
    }

    // 驗證 Token 是否合法 (帳號是否相符且未過期)
    public Boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }
}