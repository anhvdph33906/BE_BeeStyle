package com.datn.beestyle.dto.order.item;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateOrderItemRequest {
    Long id;

    Long orderId;

    @NotNull(message = "Id biến thể sản phẩm không hợp lệ (null).")
    Long productVariantId;

    @NotNull(message = "Số lượng không có giá trị.")
    @Min(value = 1, message = "Số lượng không được nhỏ hơn 1.")
    Integer quantity;

    @NotNull(message = "Giá bán không có giá trị.")
    @Min(value = 0, message = "Giá bán không được nhỏ hơn không.")
    BigDecimal salePrice;

    BigDecimal discountedPrice;
}
