package com.SISGEPAL.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;

@Component
public class JWTUtil {
    @Value("${jwt.secret}")
    private String SECRET_KEY;
    @Value("${jwt.max_time}")
    private long jwtExpireTime;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractId(String token){
        return extractClaim(token, Claims::getId);
    }

    public boolean isTokenExpired(String token) {

        boolean isExpired = extractExpiration(token).before(new Date());
        Logger log = Logger.getLogger(this.getClass().toString());
        log.info(String.format("Token expirado: %s",isExpired ));
        return isExpired;
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY)
                .parseClaimsJws(token).getBody();
    }


    public String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+jwtExpireTime))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public Claims validateToken(String token) {
        final Claims claims =
                Jwts.parser().setSigningKey(SECRET_KEY)
                        .parseClaimsJws(token).getBody();
        return claims;
    }
}
