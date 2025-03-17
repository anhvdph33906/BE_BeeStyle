package com.datn.beestyle.dto.voucher;

import com.datn.beestyle.enums.DiscountType;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateVoucherRequest {
    @NotBlank(message = "Tên voucher không được để trống")
    @Size(max = 50, message = "Tên voucher không được quá 50 ký tự")
    private String voucherName;

    @NotBlank(message = "Mã voucher không được để trống")
    @Size(max = 50, message = "Mã voucher không được quá 50 ký tự")
    private String voucherCode;

    @NotNull(message = "Loại giảm giá không được để trống")
    private String discountType;

    @Min(value = 0, message = "Giá trị giảm giá phải lớn hơn hoặc bằng 0")
    private Integer discountValue;

    @Min(value = 0, message = "Giới hạn giảm giá tối đa phải lớn hơn hoặc bằng 0")
    private Integer maxDiscount;

    @NotNull(message = "Giá trị đơn hàng tối thiểu không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá trị đơn hàng tối thiểu phải lớn hơn 0")
    private BigDecimal minOrderValue;

    @NotNull(message = "Thời gian bắt đầu không được để trống")
    private Timestamp startDate;

    @NotNull(message = "Thời gian kết thúc không được để trống")
    private Timestamp endDate;

    @Min(value = 0, message = "Giới hạn sử dụng phải lớn hơn hoặc bằng 0")
    private Integer usageLimit;

    @Min(value = 0, message = "Giới hạn sử dụng cho mỗi người dùng phải lớn hơn hoặc bằng 0")
    private Integer usagePerUser;

    private String note;

    private Integer status;
}
