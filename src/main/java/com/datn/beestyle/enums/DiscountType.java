package com.datn.beestyle.enums;

import lombok.Getter;
import org.springframework.lang.Nullable;

@Getter
public enum DiscountType {
    PERCENTAGE(0),
    CASH(1);

    private final int value;
    DiscountType(int value) {
        this.value = value;
    }

    public static DiscountType valueOf(int value) {
        DiscountType discountType = resolve(value);
        if (discountType == null) {
            throw new IllegalArgumentException("No matching constant for [" + value + "]");
        }
        return discountType;
    }

    @Nullable
    public static DiscountType resolve(int value) {
        for (DiscountType discountType : DiscountType.values()) {
            if (discountType.value == value) {
                return discountType;
            }
        }
        return null;
    }

    public static DiscountType fromString(String discountType) {
        try {
            return DiscountType.valueOf(discountType.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }
    @Nullable
    public static String fromInteger(Integer value) {
        if (value == null) return null;
        try {
            DiscountType discountType = DiscountType.resolve(value);
            return discountType != null ? discountType.name() : null;
        } catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }

}
