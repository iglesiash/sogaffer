package es.unican.hgi834.sogaffer.service.auth.impl;

import es.unican.hgi834.sogaffer.configuration.JwtPropertiesConfiguration;
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

    private final String secretKey;
    private final String aud;
    private final int durationTime;

    public JwtTokenService(JwtPropertiesConfiguration jwtConfiguration) {
        this.secretKey = jwtConfiguration.getSecret();
        this.aud = jwtConfiguration.getAud();
        this.durationTime = jwtConfiguration.getDuration();
    }

    @Override
    public AccessTokenDto generateToken(String username) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + durationTime * 1000L); // Convert to milliseconds

        String token = Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expiration)
                .issuer(aud)
                .signWith(getSigningKey())
                .compact();

        return new AccessTokenDto(token, durationTime);
    }

    @Override
    public SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
