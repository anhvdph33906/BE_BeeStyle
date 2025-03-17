package com.datn.beestyle.dto.ghtk;

import lombok.Data;

@Data
public class GhtkFeeRequest {
    private String pick_province;
    private String pick_district;
    private String province;
    private String district;
    private String address;
    private int weight;
    private int value;
    private String transport;
}
