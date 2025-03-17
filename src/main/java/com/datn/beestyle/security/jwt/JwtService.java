package com.datn.beestyle.security.jwt;

import com.datn.beestyle.enums.TokenType;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String generateAccessToken(UserDetails userDetails);
    String generateRefreshToken(UserDetails userDetails);
    String generateResetToken(UserDetails userDetails);
    String extractUsername(String token, TokenType type);
    boolean isValidToken(String token, TokenType type, UserDetails userDetails);

}
