package com.datn.beestyle.dto.cart;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ShoppingCartResponse {
    private Long id;
    private Long productVariantId;
    private Long productId;
    private Long customerId;
    private String cartCode;
    private String productName;
    private String imageUrl;
    private String sizeName;
    private String colorName;
    private Integer quantityInStock;
    private Integer quantity;
    private BigDecimal salePrice;
    private Integer totalPrice;
    private String description;

    public ShoppingCartResponse(
            Long id, Long productVariantId, Long productId,
            String cartCode, String productName, String imageUrl,
            String sizeName, String colorName, Integer quantityInStock,
            Integer quantity, BigDecimal salePrice
    ) {
        this.id = id;
        this.productVariantId = productVariantId;
        this.productId = productId;
        this.cartCode = cartCode;
        this.productName = productName;
        this.imageUrl = imageUrl;
        this.sizeName = sizeName;
        this.colorName = colorName;
        this.quantityInStock = quantityInStock;
        this.quantity = quantity;
        this.salePrice = salePrice;
    }

    public ShoppingCartResponse(
            Long id, Long productVariantId, Long customerId,
            String cartCode, Integer quantity,
            BigDecimal salePrice
    ) {
        this.id = id;
        this.productVariantId = productVariantId;
        this.customerId = customerId;
        this.cartCode = cartCode;
        this.quantity = quantity;
        this.salePrice = salePrice;
    }
}
