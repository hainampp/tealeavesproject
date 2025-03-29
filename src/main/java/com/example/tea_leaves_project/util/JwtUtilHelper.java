package com.example.tea_leaves_project.Util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.List;

@Component
public class JwtUtilHelper {
    @Value("${jwt.privateKey}")
    private String privateKey;
    // từ key tạo ra token cho user
    public String generateToken(String email, List<String> roles) {
        SecretKey key= Keys.hmacShaKeyFor(Decoders.BASE64.decode(privateKey));
        String jws= Jwts.builder()
                .setSubject(email)
                .claim("roles", roles)
                .signWith(key)
                .compact();
        return jws;
    }
    // kiểm tra token có phải do key của server tạo ra không
    public boolean verifyToken(String token) {
        boolean isVerifiy = false;
        try{
            SecretKey key= Keys.hmacShaKeyFor(Decoders.BASE64.decode(privateKey));
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            isVerifiy = true;
        } catch (Exception e) {
            System.out.println("Error verify token "+ e.getMessage());
            isVerifiy = false;
        }
        return isVerifiy;
    }
    public String getEmail(String token) {
        SecretKey key= Keys.hmacShaKeyFor(Decoders.BASE64.decode(privateKey));
        @SuppressWarnings("deprecation")
        String email = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token).getBody().getSubject();
        return email;
    }
    public List<String> extractRoles(String token) {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(privateKey));
        @SuppressWarnings("deprecation")
        Claims claims = Jwts.parserBuilder() // Dùng parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("roles", List.class);
    }
}
