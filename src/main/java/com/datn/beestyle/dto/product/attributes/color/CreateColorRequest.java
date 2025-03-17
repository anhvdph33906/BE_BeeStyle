package com.datn.beestyle.dto.product.attributes.color;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateColorRequest {

    String colorCode;

    @NotBlank(message = "Vui lòng nhập tên màu sắc.")
    String colorName;
}
