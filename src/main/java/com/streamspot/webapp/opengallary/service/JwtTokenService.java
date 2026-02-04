package com.streamspot.webapp.opengallary.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;

import java.time.Instant;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

@Service
public class JwtTokenService {

	//private final SecretKey SECRET_KEY = Keys.hmacShaKeyFor("super-secret-key-super-secret-key".getBytes());

    SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // generates a secure random key
    String secretKey = Encoders.BASE64.encode(key.getEncoded()); // store this string
    
    public String generateToken(String email) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(60 * 60))) // 1 hour expiry
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String validateAndExtractEmail(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}

