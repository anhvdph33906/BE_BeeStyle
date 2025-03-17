package com.datn.beestyle.dto.voucher;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.sql.Timestamp;


@Getter
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class UserVoucherResponse {
    Long id;
    String voucherCode;
    String voucherName;
    String discountType;
    Integer discountValue;
    Integer maxDiscount;
    BigDecimal minOrderValue;
    Timestamp startDate;
    Timestamp endDate;
    Integer usageLimit;
    Integer usagePerUser;


}
