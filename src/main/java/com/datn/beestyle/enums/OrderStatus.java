package com.datn.beestyle.enums;

import lombok.Getter;
import org.springframework.lang.Nullable;

@Getter
public enum OrderStatus {
    PENDING(0), // chờ thanh toán

    // đã thanh toán dành cho bán trực tiếp
    PAID(1),

    AWAITING_CONFIRMATION(2), // chờ xác nhận
    CONFIRMED(3), // đã xác nhận
    AWAITING_SHIPMENT(4), // chờ giao hàng
    OUT_FOR_DELIVERY(5), // đang giao hàng

    DELIVERED(6), // đã thanh toán dành cho bán giao hàng
    CANCELLED(7), // đã hủy
//    RETURN_REQUESTED(7), // yêu cầu trả hàng
    RETURNED(8), // đã trả hàng
//    REFUNDED(9) // đã hoàn tiền
    ;

    private final int value;
    OrderStatus(int value) {
        this.value = value;
    }

    public static OrderStatus valueOf(int value) {
        OrderStatus orderStatus = resolve(value);
        if (orderStatus == null) return null;
//            throw new IllegalArgumentException("No matching constant for [" + value + "]");
        return orderStatus;
    }

    @Nullable
    public static OrderStatus resolve(int value) {
        for (OrderStatus orderStatus : OrderStatus.values()) {
            if (orderStatus.value == value) {
                return orderStatus;
            }
        }
        return null;
    }

    @Nullable
    public static OrderStatus fromString(String orderStatus) {
        try {
            return OrderStatus.valueOf(orderStatus.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }

    @Nullable
    public static String fromInteger(Integer value) {
        if (value == null) return null;
        try {
            OrderStatus orderStatus = OrderStatus.resolve(value);
            return orderStatus != null ? orderStatus.name() : null;
        } catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }
}
