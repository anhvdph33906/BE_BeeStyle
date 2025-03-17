package com.datn.beestyle.security;

import com.datn.beestyle.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.annotations.Comment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * sử dụng để handle các exceptions liên quan tới việc authentication,
 * đặc biệt là các trường hợp cố gắng truy cập các tài nguyên không có quyền (unauthorized resources)
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        logger.error("Unauthorized error: {}", authException.getMessage());

        // Sử dụng ObjectMapper để chuyển đổi đối tượng errorResponse thành chuỗi JSON.
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setCode(HttpStatus.UNAUTHORIZED.value());
        errorResponse.setPath(request.getServletPath());
        errorResponse.setError(HttpStatus.UNAUTHORIZED.name());
        errorResponse.setMessage(authException.getMessage());

        // Ghi chuỗi JSON vào phản hồi HTTP
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));

        //  Đảm bảo rằng tất cả dữ liệu đã được ghi vào phản hồi HTTP.
        response.flushBuffer();
    }
}
