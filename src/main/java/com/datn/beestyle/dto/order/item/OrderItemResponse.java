package com.datn.beestyle.dto.order.item;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderItemResponse {
    Long id;
    Long orderId;
    Long productVariantId;
    String sku;
    Long productId;
    String productName;
    Integer colorId;
    String colorCode;
    String colorName;
    Integer sizeId;
    String sizeName;
    Integer quantity;
    BigDecimal salePrice;
    BigDecimal discountedPrice;
    String note;
}
