package com.datn.beestyle.dto.ghtk;

import lombok.Data;

public class GhtkFeeResponse {
    private boolean success;
    private String message;
    private Fee fee;

    @Data
    public static class Fee {
        private int main_fee;
        private int insurance_fee;
        private int total_fee;
    }
}
