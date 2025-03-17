package com.datn.beestyle.dto.promotion;

import com.datn.beestyle.enums.Status;
import com.datn.beestyle.validation.EnumValue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import com.datn.beestyle.enums.DiscountType;

import java.sql.Timestamp;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreatePromotionRequest {

    @NotBlank(message = "Promotion name cannot be blank")
    String promotionName;

    @NotNull(message = "Loại giảm giá không được để trống")
    String discountType;

    @NotNull(message = "Discount value cannot be null")
    Integer discountValue;

    @NotNull(message = "Start date cannot be blank")
    Timestamp startDate;

    @NotNull(message = "End date cannot be blank")
    Timestamp endDate;

    @NotBlank(message = "Description name cannot be blank")
    String description;

}
