package com.datn.beestyle.dto.product;

import com.datn.beestyle.dto.product.variant.CreateProductVariantRequest;
import com.datn.beestyle.entity.product.ProductImage;
import com.datn.beestyle.enums.GenderProduct;
import com.datn.beestyle.validation.EnumValue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateProductRequest {

    @NotBlank(message = "Không để trống trường")
    @Size(max = 255)
    String productName;

    String productCode;

    @EnumValue(enumClass = GenderProduct.class, name = "GenderProduct", message = "Invalid value for Gender")
    String genderProduct;

    Integer brandId;
    Integer materialId;
    Integer categoryId;
    String description;

    List<ProductImage> productImages = new ArrayList<>();

    List<CreateProductVariantRequest> productVariants = new ArrayList<>();

}
