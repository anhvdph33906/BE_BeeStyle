package com.datn.beestyle.dto.promotion;

import com.datn.beestyle.enums.DiscountStatus;
import com.datn.beestyle.enums.DiscountType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PromotionResponse{
    Integer id;
    String promotionName;
    String discountType;
    Integer discountValue;
    Timestamp startDate;
    Timestamp endDate;
    Integer createdBy;
    Integer updatedBy;
    String description;
    String status;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    public PromotionResponse(Integer id, String promotionName, Integer discountType, Integer discountValue,
                             Timestamp startDate, Timestamp endDate, Integer createdBy, Integer updatedBy,
                             String description, Integer status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.promotionName = promotionName;
        this.discountType = DiscountType.fromInteger(discountType);
        this.discountValue = discountValue;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.description = description;
        this.status = DiscountStatus.fromInteger(status);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
