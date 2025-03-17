package com.datn.beestyle.dto.product.variant;

import com.datn.beestyle.dto.product.attributes.image.ImageReponse;
import com.datn.beestyle.enums.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductVariantResponse {
    Long id;
    String sku;
    Long productId;
    String productCode;
    String productName;
    String categoryName;
    String brandName;
    Integer colorId;
    String colorCode;
    String colorName;
    Integer sizeId;
    String sizeName;
    BigDecimal salePrice;
    BigDecimal discountPrice;
    Integer discountValue;
    Integer quantityInStock;
    BigDecimal originalPrice;
    String description;
    String status;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    Long createdBy;
    Long updatedBy;
    String materialName;
    String imageUrl;
    String promotionName;
    BigDecimal totalPrice;
    List<ImageReponse> images;

    public ProductVariantResponse(Long id, String sku, Long productId, String productName, Integer colorId,
                                  String colorCode, String colorName, Integer sizeId, String sizeName, BigDecimal salePrice,
                                  Integer quantityInStock, BigDecimal originalPrice, Integer status, LocalDateTime createdAt,
                                  LocalDateTime updatedAt, Long createdBy, Long updatedBy) {
        this.id = id;
        this.sku = sku;
        this.productId = productId;
        this.productName = productName;
        this.colorId = colorId;
        this.colorCode = colorCode;
        this.colorName = colorName;
        this.sizeId = sizeId;
        this.sizeName = sizeName;
        this.salePrice = salePrice;
        this.quantityInStock = quantityInStock;
        this.originalPrice = originalPrice;
        this.status = Status.fromInteger(status);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }

    public ProductVariantResponse(Long id, String sku, Long productId, String productName, Integer colorId,
                                  String colorCode, String colorName, Integer sizeId, String sizeName,
                                  BigDecimal salePrice, Integer quantityInStock) {
        this.id = id;
        this.sku = sku;
        this.productId = productId;
        this.productName = productName;
        this.colorId = colorId;
        this.colorCode = colorCode;
        this.colorName = colorName;
        this.sizeId = sizeId;
        this.sizeName = sizeName;
        this.salePrice = salePrice;
        this.quantityInStock = quantityInStock;
    }

    public ProductVariantResponse(
            Long id, Long productId, String productCode, String productName, BigDecimal salePrice, BigDecimal discountPrice,
            Integer discountValue, String sku, String categoryName, String brandName, Integer quantity,
            String colorCode, String colorName, String sizeName, String description, List<ImageReponse> images
    ) {
        this.id = id;
        this.productId = productId;
        this.productCode = productCode;
        this.productName = productName;
        this.salePrice = salePrice;
        this.discountPrice = discountPrice;
        this.discountValue = discountValue;
        this.sku = sku;
        this.categoryName = categoryName;
        this.brandName = brandName;
        this.quantityInStock = quantity;
        this.colorCode = colorCode;
        this.colorName = colorName;
        this.sizeName = sizeName;
        this.description = description;
        this.images = images;
    }

    public ProductVariantResponse(
            Long id, Long productId, String productName, BigDecimal salePrice, BigDecimal discountPrice,
            Integer discountValue, String sku, Integer quantity, String colorName, String sizeName, BigDecimal totalPrice
    ) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.salePrice = salePrice;
        this.discountPrice = discountPrice;
        this.discountValue = discountValue;
        this.sku = sku;
        this.quantityInStock = quantity;
        this.colorName = colorName;
        this.sizeName = sizeName;
        this.totalPrice = totalPrice;
    }

    @JsonPropertyOrder({"productId", "id", "sku", "productName", "brandName", "materialName", "colorName", "sizeName", "originalPrice", "quantityInStock", "promotionName"})
    public ProductVariantResponse(Long productId, String productName, String brandName, String materialName, Long id,
                                  String sku, String colorName, String sizeName, BigDecimal originalPrice,
                                  Integer quantityInStock, String promotionName) {
        this.productId = productId;
        this.productName = productName;
        this.brandName = brandName;
        this.materialName = materialName;
        this.id = id;
        this.sku = sku;
        this.colorName = colorName;
        this.sizeName = sizeName;
        this.originalPrice = originalPrice;
        this.quantityInStock = quantityInStock;
        this.promotionName = promotionName;
    }
}
