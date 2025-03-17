package com.datn.beestyle.enums;

import lombok.Getter;
import org.springframework.lang.Nullable;

@Getter
public enum OrderChannel {
    OFFLINE(0), // tại cửa hàng
    ONLINE(1), // trực tuyến
//    SOCIAL_MEDIA(2), // mạng xã hội
//    PHONE(3) // telesales
    ;

    private final int value;
    OrderChannel(int value) {
        this.value = value;
    }

    public static OrderChannel valueOf(int value) {
        OrderChannel orderChannel = resolve(value);
        if (orderChannel == null) return null;
//            throw new IllegalArgumentException("No matching constant for [" + value + "]");
        return orderChannel;
    }

    @Nullable
    public static OrderChannel resolve(Integer value) {
        for (OrderChannel orderChannel : OrderChannel.values()) {
            if (orderChannel.value == value) {
                return orderChannel;
            }
        }
        return null;
    }

    @Nullable
    public static OrderChannel fromString(String value) {
        try {
            return OrderChannel.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }

    @Nullable
    public static String fromInteger(Integer value) {
        if (value == null) return null;
        try {
            OrderChannel orderChannel = OrderChannel.resolve(value);
            return orderChannel != null ? orderChannel.name() : null;
        } catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }
}
