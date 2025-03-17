package com.datn.beestyle.dto.category;

import com.datn.beestyle.enums.Status;
import com.datn.beestyle.validation.EnumValue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateCategoryRequest {

    @NotBlank(message = "Vui lòng nhập tên danh mục.")
    String categoryName;

    String slug;

    @Min(value = 1, message = "Giá trị cấp danh mục phải lớn hơn 0.")
    @Max(value = 3, message = "Giá trị cấp danh mục phải nhỏ hơn 3.")
    Integer level;

    @Min(value = 0, message = "Giá trị thứ tự ưu tiên phải không âm.")
    Integer priority;

    @Min(value = 1, message = "Giá trị id danh mục cha phải lớn hơn 0.")
    Integer parentCategoryId;

    @EnumValue(name = "Status", enumClass = Status.class)
    String status;
}
