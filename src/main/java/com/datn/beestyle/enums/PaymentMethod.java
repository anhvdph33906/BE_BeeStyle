package com.datn.beestyle.enums;

import lombok.Getter;
import org.springframework.lang.Nullable;

@Getter
public enum PaymentMethod {
    // thanh toán tiền mặt tại quầy (kênh bán trực tiếp)
    // thanh toán tiền mặt khi nhận hàng (kênh bán trực tuyến)
    CASH(0), // tiền mặt

    // thanh toán qua chuyển khoản ngân hàng (kênh bán trực tiếp)
    // thanh toán trước toàn bộ qua chuyển khoản trước khi giao hàng. (kênh bán trực tuyến)
    BANK_TRANSFER(1), // chuyển khoản

    // thanh toán 1 phần tền mặt vằ 1 phần qua chuyển khoản ngân hàng (kênh bán trực tiếp)
    // thanh toán một phần qua chuyển khoản trước, phần còn lại bằng tiền mặt khi nhận hàng. (kênh bán trực tuyến)
    CASH_AND_BANK_TRANSFER(2); // thanh toán và chuyển khoản

    private final int value;

    PaymentMethod(int value) {
        this.value = value;
    }

    public static PaymentMethod valueOf(int value) {
        PaymentMethod paymentMethod = resolve(value);
        if (paymentMethod == null) {
            throw new IllegalArgumentException("No matching constant for [" + value + "]");
        }
        return paymentMethod;
    }

    @Nullable
    public static PaymentMethod resolve(int value) {
        for (PaymentMethod paymentMethod : PaymentMethod.values()) {
            if (paymentMethod.value == value) {
                return paymentMethod;
            }
        }
        return null;
    }

    @Nullable
    public static PaymentMethod fromString(String value) {
        try {
            return PaymentMethod.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }

    @Nullable
    public static String fromInteger(Integer value) {
        if (value == null) return null;
        try {
            PaymentMethod paymentMethod = PaymentMethod.resolve(value);
            return paymentMethod != null ? paymentMethod.name() : null;
        } catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }

}
