package com.datn.beestyle.enums;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.lang.Nullable;
@Log4j2
@Getter
public enum Status {

    ACTIVE(1),
    INACTIVE(0);

    private final int value;
    Status(int value) {
        this.value = value;
    }
    public static Status valueOf(int value) {
        Status status = resolve(value);
        if (status == null) {
            log.error("No matching constant for [" + value + "]");
            return null;
        }
        return status;
    }

    @Nullable
    public static Status resolve(int value) {
        for (Status status : Status.values()) {
            if (status.value == value) {
                return status;
            }
        }
        return null;
    }

    @Nullable
    public static Status fromString(String status) {
        try {
            return Status.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }

    @Nullable
    public static String fromInteger(Integer value) {
        if (value == null) return null;
        try {
            Status status = Status.resolve(value);
            return status != null ? status.name() : null;
        } catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }


}
