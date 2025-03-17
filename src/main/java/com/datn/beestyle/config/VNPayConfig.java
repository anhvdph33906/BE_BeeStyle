package com.datn.beestyle.config;

import org.springframework.beans.factory.annotation.Value;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class VNPayConfig {

    @Value("${vnpay.tmn_code}")
    protected String vnp_TmnCode;

    @Value("${vnpay.hash_secret}")
    protected String vnp_HashSecret;

    @Value("${vnpay.pay_url}")
    protected String vnp_PayUrl;

    @Value("${vnpay.return_url}")
    protected String vnp_ReturnUrl;

    protected String HmacSHA512(String key, String data) {
        try {
            Mac hmacSHA512 = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "HmacSHA512");
            hmacSHA512.init(secretKey);
            byte[] bytes = hmacSHA512.doFinal(data.getBytes());
            StringBuilder hash = new StringBuilder();
            for (byte b : bytes) {
                hash.append(String.format("%02x", b));
            }
            return hash.toString();
        } catch (Exception ex) {
            throw new RuntimeException("Error while hashing data", ex);
        }
    }

    /**
     * Kiểm tra tính toàn vẹn của dữ liệu trả về từ VNPay bằng cách xác thực vnp_SecureHash.
     */
    private boolean isValidVNPayResponse(Map<String, String> params) {
        String vnp_SecureHash = params.get("vnp_SecureHash");
        params.remove("vnp_SecureHash"); // Loại bỏ hash để tính lại

        // Sắp xếp các tham số theo thứ tự key
        List<String> fieldNames = new ArrayList<>(params.keySet());
        Collections.sort(fieldNames);

        // Tạo chuỗi dữ liệu để hash
        StringBuilder hashData = new StringBuilder();
        for (String fieldName : fieldNames) {
            String fieldValue = params.get(fieldName);
            if (fieldValue != null && fieldValue.length() > 0) {
                hashData.append(fieldName).append("=").append(fieldValue).append("&");
            }
        }
        hashData.deleteCharAt(hashData.length() - 1);

        // Hash dữ liệu với key bí mật
        String computedHash = HmacSHA512(vnp_HashSecret, hashData.toString());

        // So sánh hash trả về với hash đã tính
        return computedHash.equals(vnp_SecureHash);
    }
}
