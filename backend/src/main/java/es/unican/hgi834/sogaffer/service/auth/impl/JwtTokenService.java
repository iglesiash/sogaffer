package es.unican.hgi834.sogaffer.service.auth.impl;

import es.unican.hgi834.sogaffer.model.dto.auth.AccessTokenDto;
import es.unican.hgi834.sogaffer.service.auth.IJwtTokenService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.io.Decoders;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
public class JwtTokenService implements IJwtTokenService {

    @Value("${sogaffer.jwt.access.token}")
    private String secretKey;

    private static final int EXPIRATION_TIME_MS = 1000 * 60 * 60; // 1 hour

    @Override
    public AccessTokenDto generateToken(String username) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_TIME_MS);

        String token = Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expiration)
                .issuer("SoGaffer")
                .signWith(getSigningKey())
                .compact();

        return new AccessTokenDto(token, EXPIRATION_TIME_MS);
    }

    @Override
    public SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
