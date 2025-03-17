package com.datn.beestyle.dto.product.attributes.size;

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
public class UpdateSizeRequest {

    Integer id;

    @NotBlank(message = "Vui lòng nhập tên kích thước.")
    String sizeName;

    @EnumValue(name = "Status", enumClass = Status.class)
    String status;
}
