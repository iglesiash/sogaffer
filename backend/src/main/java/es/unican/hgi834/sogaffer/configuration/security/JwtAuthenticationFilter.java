package es.unican.hgi834.sogaffer.configuration.security;

import es.unican.hgi834.sogaffer.service.auth.IJwtTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final static String BEARER_PREFIX = "Bearer ";

    private final IJwtTokenService jwtTokenService;

    public JwtAuthenticationFilter(IJwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {

            try {
                String token = authHeader.substring(BEARER_PREFIX.length());

                Claims claims = Jwts.parser()
                        .verifyWith(jwtTokenService.getSigningKey())
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();

                UsernamePasswordAuthenticationToken authentication = getUsernamePasswordAuthenticationToken(claims);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Save the user in the security context
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                SecurityContextHolder.clearContext();
                logger.error(e.getMessage(), e);
            }

        }

        filterChain.doFilter(request, response);
    }

    private static UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(Claims claims) {
        String issuer = claims.getIssuer();
        Date expirationDate = claims.getExpiration();

        if (issuer == null || !issuer.equals("SoGaffer")) {
            throw new SecurityException("Invalid issuer");
        }

        if (expirationDate.before(new Date())) {
            throw new SecurityException("Expired token at " + expirationDate);
        }

        User user = new User(claims.getSubject(), null, Collections.emptyList());
        return new UsernamePasswordAuthenticationToken(user,
                null, // No credentials
                user.getAuthorities());
    }
}