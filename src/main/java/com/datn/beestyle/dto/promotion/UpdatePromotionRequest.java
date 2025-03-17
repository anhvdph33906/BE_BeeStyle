package com.datn.beestyle.dto.promotion;

import com.datn.beestyle.enums.DiscountStatus;
import com.datn.beestyle.enums.DiscountType;
import com.datn.beestyle.enums.Status;
import com.datn.beestyle.validation.EnumValue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdatePromotionRequest {

    @NotNull(message = "ID cannot be null")
    Integer id;

    @NotBlank(message = "Promotion name cannot be blank")
    String promotionName;

    @NotNull(message = "Discount type cannot be null")
    String discountType;

    @NotNull(message = "Discount value cannot be null")
    @Positive(message = "Discount value must be positive")
    Integer discountValue;

    @NotNull(message = "Start date cannot be null")
    Timestamp startDate;

    @NotNull(message = "End date cannot be null")
    Timestamp endDate;

    String description;
//    @EnumValue(name = "Status", enumClass = DiscountStatus.class)
//    String status;
}
