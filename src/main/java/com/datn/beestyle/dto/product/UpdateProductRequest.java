package com.datn.beestyle.dto.product;

import com.datn.beestyle.enums.Gender;
import com.datn.beestyle.enums.GenderProduct;
import com.datn.beestyle.enums.Status;
import com.datn.beestyle.validation.EnumValue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateProductRequest {

    @NotBlank(message = "Vui lòng nhập tên sản phẩm.")
    @Size(max = 255)
    String productName;

    String productCode;

    String imageUrl;

    @EnumValue(enumClass = GenderProduct.class, name = "GenderProduct", message = "Invalid value for Gender")
    String genderProduct;

    Integer brandId;
    Integer materialId;
    Integer categoryId;
    String description;

    @EnumValue(enumClass = Status.class, name = "Status", message = "Invalid value for Status")
    String status;
}
