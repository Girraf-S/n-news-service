package com.solbeg.nnewsservice.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Service
public class JwtService {

    @Value("${jwt.key}")
    private String jwtSecret;


    @PostConstruct
    protected void init() {
        jwtSecret = Base64.getEncoder().encodeToString(jwtSecret.getBytes());
    }

    public DecodedJWT decodeJWT(String token) {
        JWTVerifier verifier = JWT
                .require(Algorithm.HMAC256(jwtSecret))
                .build();
        return Optional.ofNullable(verifier.verify(token))
                .orElseThrow(() -> new RuntimeException("Invalid token " + token));
    }

    public String extractUsername(String token) {
        DecodedJWT decodedJWT = decodeJWT(token);
        return decodedJWT.getSubject();
    }

    public boolean isTokenValid(String token, String username){
        final String extractedUsername = extractUsername(token);
        return (username.equals(extractedUsername)) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return decodeJWT(token).getExpiresAt();
    }
}
