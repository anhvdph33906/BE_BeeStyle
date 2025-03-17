package com.datn.beestyle.dto.voucher;

import com.datn.beestyle.enums.DiscountStatus;
import com.datn.beestyle.enums.DiscountType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoucherResponse {
    Integer id;
    String voucherName;
    String voucherCode;
    String discountType;
    Integer discountValue;
    Integer maxDiscount;
    BigDecimal minOrderValue;
    Timestamp startDate;
    Timestamp endDate;
    Integer usageLimit;
    Integer usagePerUser;
    String note;
    String status;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    public VoucherResponse(Integer id, String voucherName, String voucherCode, Integer discountType,
                           Integer discountValue, Integer maxDiscount, BigDecimal minOrderValue,
                           Timestamp startDate, Timestamp endDate, Integer usageLimit,
                           Integer usagePerUser, String note, Integer status,
                           LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.voucherName = voucherName;
        this.voucherCode = voucherCode;
        this.discountType = DiscountType.fromInteger(discountType);
        this.discountValue = discountValue;
        this.maxDiscount = maxDiscount;
        this.minOrderValue = minOrderValue;
        this.startDate = startDate;
        this.endDate = endDate;
        this.usageLimit = usageLimit;
        this.usagePerUser = usagePerUser;
        this.note = note;
        this.status = DiscountStatus.fromInteger(status);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
