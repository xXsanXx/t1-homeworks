package com.example.authorization.config.jwt;

import com.example.authorization.service.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

@Component
public class JwtUtils {

    private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    @Value("${authorization.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder().setSubject((userPrincipal.getUsername())).setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, secretKey).compact();
    }

    public boolean validateJwtToken(String jwt) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwt);
            return true;
        } catch (MalformedJwtException e) {
            System.err.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }

        return false;
    }

    public String getUserNameFromJwtToken(String jwt) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwt).getBody().getSubject();
    }

}
