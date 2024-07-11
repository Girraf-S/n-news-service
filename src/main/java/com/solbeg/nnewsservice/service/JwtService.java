package com.solbeg.nnewsservice.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class JwtService {

    @Value("${jwt.key}")
    private String jwtSecret;
    @Value("${jwt.bearer}")
    private String bearer;
    @Value("${jwt.begin-index}")
    private int beginIndex;

    @PostConstruct
    protected void init() {
        jwtSecret = Base64.getEncoder().encodeToString(jwtSecret.getBytes());
    }

    private DecodedJWT decodeJWT(String token) {
        var verifier = JWT
                .require(Algorithm.HMAC256(jwtSecret))
                .build();
        return Optional.ofNullable(verifier.verify(token))
                .orElseThrow(() -> new RuntimeException("Invalid token " + token));
    }

    public String getJwtOrThrowException(String header){
        if(!header.startsWith(bearer))
            throw new JWTVerificationException("Header should be started with 'Bearer'");
        return header.substring(beginIndex);
    }

    public String extractUsername(String token) {
        DecodedJWT decodedJWT = decodeJWT(token);
        return decodedJWT.getSubject();
    }
    public Long extractId(String token){
        Long id = decodeJWT(token).getClaim("id").asLong();
        return decodeJWT(token).getClaim("id").asLong();
    }

    public Set<SimpleGrantedAuthority> extractAuthorities(String token){
        return decodeJWT(token).getClaim("authorities").asList(String.class)
                .stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
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
