package com.datn.beestyle.dto.order;

import com.datn.beestyle.enums.OrderChannel;
import com.datn.beestyle.enums.OrderStatus;
import com.datn.beestyle.enums.OrderType;
import com.datn.beestyle.enums.PaymentMethod;
import com.datn.beestyle.validation.EnumValue;
import com.datn.beestyle.validation.PhoneNumber;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateOrderRequest {
    Long id;

    Long customerId;
    Integer voucherId;
    String receiverName;

    String phoneNumber;

    @Min(value = 0, message = "Shipping fee is not valid (value >= 0).")
    BigDecimal shippingFee;

    @Min(value = 0, message = "Original amount is not valid.")
    BigDecimal originalAmount;

    @Min(value = 0, message = "Discount amount is not valid.")
    BigDecimal discountAmount;

    @Min(value = 0, message = "Total amount is not valid.")
    BigDecimal totalAmount;

    @Min(value = 0, message = "Amount paid is not valid.")
    BigDecimal amountPaid;

    @EnumValue(enumClass = PaymentMethod.class, name = "PaymentMethod", message = "Phương thức thanh toán không hợp lệ.")
    String paymentMethod;

    @EnumValue(enumClass = OrderType.class, name = "OrderType", message = "Loại hóa đơn không hợp lệ.")
    String orderType;

    @EnumValue(enumClass = OrderChannel.class, name = "OrderChannel", message = "Kênh bán hàng không hợp lệ.")
    String orderChannel;

    @EnumValue(enumClass = OrderStatus.class, name = "OrderStatus", message = "Trạng thái hóa đơn không hợp lệ")
    String orderStatus;

    Long shippingAddressId;

    String note;
}
