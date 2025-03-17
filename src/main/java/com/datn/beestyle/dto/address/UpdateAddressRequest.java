package com.datn.beestyle.dto.address;

import com.datn.beestyle.entity.user.Customer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateAddressRequest {
    @NotBlank(message = "Không được để trống chi tiết")
    String addressName;

    @NotNull(message = "Không được để trống cityCode")
    Integer cityCode;
    @NotBlank(message = "Không được để trống tỉnh")
    String city;
    @NotNull(message = "Không được để trống districtCode")
    Integer districtCode;
    @NotBlank(message = "Không được để trống huyện")
    String district;
    @NotNull(message = "Không được để trống communeCode")
    Integer communeCode;
    @NotBlank(message = "Không được để trống xã")
    String commune;

    Boolean isDefault;
}
