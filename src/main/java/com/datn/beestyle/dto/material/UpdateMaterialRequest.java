package com.datn.beestyle.dto.material;

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
public class UpdateMaterialRequest {

    Integer id;

    @NotBlank(message = "Vui lòng nhập tên chất liệu")
    String materialName;

    @EnumValue(name = "Status", enumClass = Status.class)
    String status;
}
