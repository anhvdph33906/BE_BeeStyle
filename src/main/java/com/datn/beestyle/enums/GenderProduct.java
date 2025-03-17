package com.datn.beestyle.enums;

import lombok.Getter;
import org.springframework.lang.Nullable;

@Getter
public enum GenderProduct {
    MALE(0),
    FEMALE(1),
    UNISEX(2);

    private final int value;

    GenderProduct(int value) {
        this.value = value;
    }

    public static GenderProduct valueOf(int value) {
        GenderProduct genderProduct = resolve(value);
        if (genderProduct == null) {
            throw new IllegalArgumentException("No matching constant for [" + value + "]");
        }
        return genderProduct;
    }

    @Nullable
    public static GenderProduct resolve(int value) {
        for (GenderProduct genderProduct : GenderProduct.values()) {
            if (genderProduct.value == value) {
                return genderProduct;
            }
        }
        return null;
    }

    @Nullable
    public static GenderProduct fromString(String status) {
        try {
            return GenderProduct.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }

    @Nullable
    public static String fromInteger(Integer value) {
        if (value == null) return null;
        try {
            GenderProduct genderProduct = GenderProduct.resolve(value);
            return genderProduct != null ? genderProduct.name() : null;
        } catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }

}
