package com.datn.beestyle.dto.vnpay;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString
public class PaymentRequest {
    private String orderId;    // Mã đơn hàng
    private Long amount;       // Tổng tiền đơn hàng
    private String ipAddress;  // Địa chỉ IP của client
    private String bankCode;   // Mã ngân hàng được chọn
}
