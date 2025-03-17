package com.datn.beestyle.dto.product.variant;

import com.datn.beestyle.enums.Status;
import com.datn.beestyle.validation.EnumValue;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateProductVariantRequest {
    Integer id;
    String sku;
    Integer productId;
    Integer colorId;
    Integer sizeId;
    BigDecimal originalPrice = BigDecimal.ZERO;
    BigDecimal salePrice = BigDecimal.ZERO;
    Integer quantityInStock = 0;

    @EnumValue(enumClass = Status.class, name = "Status", message = "Invalid value for Status")
    String status;

    private Integer promotionId;
    private List<Integer> variantIds;

}
