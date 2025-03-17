package com.datn.beestyle.dto.brand;

import com.datn.beestyle.enums.Status;
import com.datn.beestyle.validation.EnumValue;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateBrandRequest {

    Integer id;

    @NotBlank(message = "Vui lòng nhập tên thương hiệu")
    String brandName;

    @EnumValue(name = "Status", enumClass = Status.class)
    String status;
}
