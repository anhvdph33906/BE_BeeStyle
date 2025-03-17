package com.datn.beestyle.security.jwt;

import com.datn.beestyle.enums.TokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.datn.beestyle.enums.TokenType.*;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.expiryHour}")
    private long expiryHour;

    @Value("${jwt.expiryDay}")
    private long expiryDay;

    @Value("${jwt.accessKey}")
    private String accessKey;

    @Value("${jwt.refreshKey}")
    private String refreshKey;

    @Value("${jwt.resetKey}")
    private String resetKey;

    /**
     * tạo ACCESS_TOKEN
     * @param userDetails
     * @return
     */
    @Override
    public String generateAccessToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails, ACCESS_TOKEN);
    }

    /**
     * tạo REFRESH_TOKEN
     * @param userDetails
     * @return
     */
    @Override
    public String generateRefreshToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails, REFRESH_TOKEN);
    }

    /**
     * tạo RESET_TOKEN
     * @param userDetails
     * @return
     */
    @Override
    public String generateResetToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails, RESET_TOKEN);
    }

    /**
     * Giải mã token lấy Username
     * @param token
     * @param type
     * @return
     */
    @Override
    public String extractUsername(String token, TokenType type) {
        return extractClaim(token, type, Claims::getSubject);
    }

    @Override
    public boolean isValidToken(String token, TokenType type, UserDetails userDetails) {
        final String username = extractUsername(token, type);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token, type);
    }

    private String generateToken(Map<String, Object> claims, UserDetails userDetails, TokenType type) {
        long expiration = 1000 * 60 * 60;
        if (ACCESS_TOKEN.equals(type)) {
            expiration *= expiryHour; // thời gian hết hạn access token
        } else if (REFRESH_TOKEN.equals(type)) {
            expiration *= expiryDay; // thời gian hết hạn refresh token
        } else {
            expiration *= expiryHour; // thời gian hết hạn reset token
        }

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis())) // ngày tạo token
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // thời gian hết hạn
                .signWith(getKey(type), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKey(TokenType type) {
        byte[] key;
        if (ACCESS_TOKEN.equals(type)) {
            key = Decoders.BASE64.decode(accessKey);
        } else if (REFRESH_TOKEN.equals(type)) {
            key = Decoders.BASE64.decode(refreshKey);
        } else {
            key = Decoders.BASE64.decode(resetKey);
        }
        return Keys.hmacShaKeyFor(key);
    }

    private <T> T extractClaim(String token, TokenType type, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token, type);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token, TokenType type) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey(type))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * kiểm tra token đã hết hạn chưa
     * @param token
     * @param type
     * @return
     */
    private boolean isTokenExpired(String token, TokenType type) {
        return extractExpiration(token, type).before(new Date());
    }

    private Date extractExpiration(String token, TokenType type) {
        return extractClaim(token, type, Claims::getExpiration);
    }
}
