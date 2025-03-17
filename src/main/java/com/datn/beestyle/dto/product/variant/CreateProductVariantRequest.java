package com.datn.beestyle.dto.product.variant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateProductVariantRequest {
    String sku;
    Integer productId;
    Integer colorId;
    Integer sizeId;
    BigDecimal originalPrice = BigDecimal.ZERO;
    BigDecimal salePrice = BigDecimal.ZERO;
    Integer quantityInStock = 0;
}
