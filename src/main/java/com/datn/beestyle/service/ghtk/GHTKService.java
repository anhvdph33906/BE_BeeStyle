package com.datn.beestyle.service.ghtk;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GHTKService {

    @Value("${ghtk.api.token}")
    private String apiToken;

    @Value("${ghtk.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    public GHTKService(RestTemplate restTemplate, HttpSession httpSession) {
        this.restTemplate = restTemplate;
    }

    // Phương thức tạo headers cho request
    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Token", apiToken);  // Thêm token vào headers
        return headers;
    }

    // Phương thức tính phí vận chuyển trả về ResponseEntity, không sử dụng DTO
    public ResponseEntity<String> calculateShippingFee(Object request) {
        HttpHeaders headers = createHeaders();  // Sử dụng phương thức tạo headers

        // Chuyển request và headers thành HttpEntity
        HttpEntity<Object> httpEntity = new HttpEntity<>(request, headers);

        try {
            // Gửi POST request đến GHTK API và nhận phản hồi
            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, httpEntity, String.class);

            // Trả về phản hồi từ GHTK API với mã trạng thái và dữ liệu
            return new ResponseEntity<>(response.getBody(), response.getStatusCode());
        } catch (Exception e) {
            // Log lỗi và ném exception
            throw new RuntimeException("Failed to call GHTK API: " + e.getMessage(), e);
        }
    }
}