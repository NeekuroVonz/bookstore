package com.kamikaze.bookstore.hung.security.jwt;

import java.util.Base64;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.kamikaze.bookstore.hung.security.service.UsersDetailsImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;

/*
 * 
 * reading this Jwt docs make my eyes burning but it worth it!
 * @author NekooiTine
 * p/s: i did entire security or maybe entire backend lol
 * 
 */
@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    
    @Value("${jwt.expirationMs}")
    private int jwtExpirationMs;

    // @Value("${jwt.secret}")
    // private String jwtSecretString;
    
    // im got this long long token randomly on internet and dont remember where did i have it xD
    private byte[] jwtSecret = Base64.getMimeDecoder().decode("eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwczovL2p3dC1pZHAuZXhhbXBsZS5jb20iLCJzdWIiOiJtYWlsdG86bWlrZUBleGFtcGxlLmNvbSIsIm5iZiI6MTY1ODgwNzgxNCwiZXhwIjoxNjU4ODExNDE0LCJpYXQiOjE2NTg4MDc4MTQsImp0aSI6ImlkMTIzNDU2IiwidHlwIjoiaHR0cHM6Ly9leGFtcGxlLmNvbS9yZWdpc3RlciJ9.2MWdBk_HGEHe7ZyyCmprzhYR7aJYATJHYYhlN-4L9ENy4FcLnHBeMhHPElTF5MToCIAoyDvvIik2SphCRY5VCA");

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(jwtSecret).build().parse(authToken);
            return true;
        } catch (SecurityException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
       
        return false;
    }

    public String generateJwtToken(Authentication authentication) {
        UsersDetailsImpl principal = (UsersDetailsImpl)authentication.getPrincipal();
        return Jwts.builder().setSubject(principal.getUsername())
            .setIssuedAt(new Date())
            .setExpiration(new Date(new Date().getTime() + jwtExpirationMs))
            .signWith(Keys.hmacShaKeyFor(jwtSecret), SignatureAlgorithm.HS512)
            .compact();  
    }

    public String getUsernameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(jwtSecret)
            .build().parseClaimsJws(token).getBody().getSubject();
    }

}
