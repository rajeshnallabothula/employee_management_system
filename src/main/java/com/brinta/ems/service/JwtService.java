package com.brinta.ems.service;

import com.brinta.ems.dto.TokenPair;
import com.brinta.ems.entity.User;
import com.brinta.ems.exception.exceptionHandler.JwtExpiredException;
import com.brinta.ems.exception.exceptionHandler.JwtInvalidException;
import com.brinta.ems.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class JwtService {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration}")
    private long jwtExpirationMs;

    @Value("${app.jwt.refresh-expiration}")
    private long refreshExpirationMs;

    @Autowired
    private UserRepository userRepositor;

    public TokenPair generateTokenPair(Authentication authentication) {
        String accessToken = generateAccessToken(authentication);
        String refreshToken = generateRefreshToken(authentication);
        return new TokenPair(accessToken, refreshToken);
    }

    public String generateAccessToken(Authentication authentication) {
        return generateToken(authentication, jwtExpirationMs, new HashMap<>());
    }

    public String generateRefreshToken(Authentication authentication) {
        Map<String, String> extraClaims = new HashMap<>();
        extraClaims.put("tokenType", "refresh");
        return generateToken(authentication, refreshExpirationMs, extraClaims);
    }

    private String generateToken(Authentication authentication, long expirationInMs,
                                 Map<String, String> extraClaims) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

        Map<String, Object> claims = new HashMap<>(extraClaims);

        claims.put("roles", authentication.getAuthorities().stream()
                .map(auth -> auth.getAuthority().replace("ROLE_", ""))
                .collect(Collectors.toList()));

        // ✅ Fetch full user from DB
        User user = userRepositor.findByUsername(userPrincipal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userPrincipal.getUsername()));

        // ✅ Inject tenantId (as Long)
        claims.put("tenantId", user.getTenant().getId());

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationInMs);

        return Jwts.builder()
                .header()
                .add("typ", "JWT")
                .and()
                .subject(user.getUsername())
                .claims(claims)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSignInKey())
                .compact();
    }

    public boolean isRefreshToken(String token) {
        Claims claims = extractAllClaims(token);
        return claims != null && "refresh".equals(claims.get("tokenType"));
    }

    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException ex) {
            log.warn("Token expired: {}", ex.getMessage());
            throw new JwtExpiredException("Token expired", ex);
        } catch (JwtException | IllegalArgumentException ex) {
            log.warn("Token invalid: {}", ex.getMessage());
            throw new JwtInvalidException("Invalid token", ex);
        }
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsernameFromToken(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }

    public boolean isValidToken(String token) {
        extractAllClaims(token); // throws if invalid
        return true;
    }

    public boolean validateTokenForUser(String token, UserDetails userDetails) {
        String username = extractUsernameFromToken(token);
        return username != null && username.equals(userDetails.getUsername());
    }

    public TokenPair generateTokenPairForSuperAdmin(Authentication authentication) {
        String accessToken = generateTokenWithoutTenant(authentication, jwtExpirationMs, new HashMap<>());
        String refreshToken = generateTokenWithoutTenant(authentication, refreshExpirationMs, Map.of("tokenType", "refresh"));
        return new TokenPair(accessToken, refreshToken);
    }

    private String generateTokenWithoutTenant(Authentication authentication, long expirationInMs,
                                              Map<String, String> extraClaims) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

        Map<String, Object> claims = new HashMap<>(extraClaims);

        claims.put("roles", authentication.getAuthorities().stream()
                .map(auth -> auth.getAuthority().replace("ROLE_", ""))
                .collect(Collectors.toList()));

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationInMs);

        return Jwts.builder()
                .header()
                .add("typ", "JWT")
                .and()
                .subject(userPrincipal.getUsername())
                .claims(claims)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSignInKey())
                .compact();
    }

}
