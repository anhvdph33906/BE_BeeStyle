package com.datn.beestyle.security.authentication;


import com.datn.beestyle.security.request.ResetPasswordRequest;
import com.datn.beestyle.security.request.SignInRequest;
import com.datn.beestyle.security.response.TokenResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthenticationService {
    TokenResponse accessToken(SignInRequest request);
    TokenResponse refreshToken(HttpServletRequest request);
    String removeToken(HttpServletRequest request);
    String forgotPassword(String email);
    String resetPassword(String secretKey);
    String changePassword(ResetPasswordRequest request);
}
