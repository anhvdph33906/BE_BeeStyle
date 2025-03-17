package com.datn.beestyle.enums;

import lombok.Getter;
import org.springframework.lang.Nullable;

@Getter
public enum OrderType {
    IN_STORE_PURCHASE(0), // Mua trực tiếp
    DELIVERY(1), // Giao hàng
    ;

    private final int value;
    OrderType(int value) {
        this.value = value;
    }

    public static OrderType valueOf(int value) {
        OrderType orderType = resolve(value);
        if (orderType == null) return null;
//            throw new IllegalArgumentException("No matching constant for [" + value + "]");
        return orderType;
    }

    @Nullable
    public static OrderType resolve(int value) {
        for (OrderType orderType : OrderType.values()) {
            if (orderType.value == value) {
                return orderType;
            }
        }
        return null;
    }

    @Nullable
    public static OrderType fromString(String orderType) {
        try {
            return OrderType.valueOf(orderType.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }

    @Nullable
    public static String fromInteger(Integer value) {
        if (value == null) return null;
        try {
            OrderType orderType = OrderType.resolve(value);
            return orderType != null ? orderType.name() : null;
        } catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }
}
