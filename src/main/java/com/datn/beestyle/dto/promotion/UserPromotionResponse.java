package com.datn.beestyle.dto.promotion;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;

@Getter
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserPromotionResponse {
    Integer id;
    String promotionName;
    String discountType;
    Integer discountValue;
    Timestamp startDate;
    Timestamp endDate;
    Integer createdBy;
    Integer updatedBy;
    String description;
}
