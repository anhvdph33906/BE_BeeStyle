package com.datn.beestyle.dto.order.item;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PatchUpdateQuantityOrderItem {
    @NotNull(message = "Id hóa đơn chi tiết không hợp lệ (null).")
    Long id;

    @NotNull(message = "Id biến thể sản phẩm không hợp lệ (null).")
    Long productVariantId;

    @NotNull(message = "Số lượng không có giá trị.")
    @Min(value = 1, message = "Số lượng không được nhỏ hơn 1.")
    Integer quantity;
}
