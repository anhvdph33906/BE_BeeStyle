package com.datn.beestyle.dto.product.attributes.size;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateSizeRequest {

    @NotBlank(message = "Vui lòng nhập tên kích thước.")
    String sizeName;

    String status;
}
