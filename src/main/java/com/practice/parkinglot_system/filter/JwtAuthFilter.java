package com.practice.parkinglot_system.filter;

import com.practice.parkinglot_system.util.JwtToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtToken jwtToken;

    public JwtAuthFilter(JwtToken jwtToken) {
        this.jwtToken = jwtToken;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // 1. 從請求標頭中獲取 "Authorization" 的內容
        final String authHeader = request.getHeader("Authorization");
        final String username;
        final String token;

        // 2. 如果沒有標頭，或者不是以 "Bearer " 開頭，就直接放行給後面的安檢機制處理 (通常就是擋下)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. 擷取 "Bearer " 後面的真實 Token 字串
        token = authHeader.substring(7);
        // 4. 透過我們寫好的工具類別解析出帳號
        username = jwtToken.extractUsername(token);

        // 5. 如果有帳號，且目前系統中還沒有這名使用者的驗證紀錄
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            
            // 6. 驗證 Token 是否合法未過期
            if (jwtToken.validateToken(token, username)) {
                // 產生一個代表驗證通過的 Token 物件 (這裡我們暫時不設定複雜的權限 Role，傳入空的 ArrayList)
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        username, null, new ArrayList<>()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // 7. 正式將使用者資訊存入 Security 的安全上下文中
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}