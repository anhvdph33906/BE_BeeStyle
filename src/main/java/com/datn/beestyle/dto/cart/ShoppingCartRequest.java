package com.datn.beestyle.dto.cart;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShoppingCartRequest {
    private Long id;

    @NotNull
    private Long productVariantId;

    @NotNull
    private Long customerId;

    @NotBlank
    private String cartCode;

    @NotNull
    @Min(value = 1)
    private Integer quantity;

    @NotNull
    @Min(value = 0)
    BigDecimal salePrice;
}
