package com.datn.beestyle.dto.product;

import com.datn.beestyle.dto.product.attributes.image.ImageReponse;
import com.datn.beestyle.enums.GenderProduct;
import com.datn.beestyle.enums.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
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
public class ProductResponse {
    Long id;
    String productCode;
    String productName;
    String imageUrl;
    Long totalProductInStock;
    BigDecimal minSalePrice;
    BigDecimal minDiscountedPrice;
    Integer discountValue;
    String genderProduct;
    Integer brandId;
    String brandName;
    Integer materialId;
    String materialName;
    Integer categoryId;
    String categoryName;
    String description;
    String status;
    List<ImageReponse> images;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    Long createdBy;
    Long updatedBy;

    public ProductResponse(
            Long id, String productName, BigDecimal minSalePrice,
            BigDecimal minDiscountedPrice, Integer discountValue, List<ImageReponse> images
    ) {
        this.id = id;
        this.productName = productName;
        this.minSalePrice = minSalePrice;
        this.minDiscountedPrice = minDiscountedPrice;
        this.images = images;
        this.discountValue = discountValue;
    }

    public ProductResponse(Long id, String productCode, String productName, String imageUrl, BigDecimal salePrice,
                           Long totalProductInStock) {
        this.id = id;
        this.productCode = productCode;
        this.productName = productName;
        this.imageUrl = imageUrl;
        this.totalProductInStock = totalProductInStock;
        this.minSalePrice = salePrice;
    }

    public ProductResponse(Long id, String productCode, String productName, String imageUrl, Integer genderProduct,
                           Integer brandId, String brandName, Integer materialId, String materialName, Integer categoryId,
                           String categoryName, String description, Integer status, LocalDateTime createdAt, LocalDateTime updatedAt,
                           Long createdBy, Long updatedBy) {
        this.id = id;
        this.productCode = productCode;
        this.productName = productName;
        this.imageUrl = imageUrl;
        this.genderProduct = GenderProduct.fromInteger(genderProduct);
        this.brandId = brandId;
        this.brandName = brandName;
        this.materialId = materialId;
        this.materialName = materialName;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.description = description;
        this.status = Status.fromInteger(status);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }



}
