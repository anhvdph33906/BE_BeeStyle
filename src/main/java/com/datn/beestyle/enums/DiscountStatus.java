package com.datn.beestyle.enums;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.lang.Nullable;

@Log4j2
@Getter
public enum DiscountStatus {

    UPCOMING(0),
    ACTIVE(1),
    EXPIRED(2);

    private final int value;

    DiscountStatus(int value) {
        this.value = value;
    }

    public static DiscountStatus valueOf(int value) {
        DiscountStatus status = resolve(value);
        if (status == null) {
            log.error("No matching constant for [" + value + "]");
            return null;
        }
        return status;
    }

    @Nullable
    public static DiscountStatus resolve(int value) {
        for (DiscountStatus status : DiscountStatus.values()) {
            if (status.value == value) {
                return status;
            }
        }
        return null;
    }

    @Nullable
    public static DiscountStatus fromString(String status) {
        try {
            return DiscountStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            log.error("Invalid status string: [" + status + "]", e);
            return null;
        }
    }

    @Nullable
    public static String fromInteger(Integer value) {
        if (value == null) return null;
        DiscountStatus status = DiscountStatus.resolve(value);
        return status != null ? status.name() : null;
    }
}
