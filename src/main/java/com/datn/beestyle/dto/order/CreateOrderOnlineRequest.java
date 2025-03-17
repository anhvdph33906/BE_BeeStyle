package com.datn.beestyle.dto.order;

import com.datn.beestyle.dto.order.item.CreateOrderItemOnlineRequest;
import com.datn.beestyle.enums.OrderChannel;
import com.datn.beestyle.enums.OrderStatus;
import com.datn.beestyle.enums.OrderType;
import com.datn.beestyle.enums.PaymentMethod;
import com.datn.beestyle.validation.EnumValue;
import com.datn.beestyle.validation.PhoneNumber;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateOrderOnlineRequest implements Serializable {
    Long customerId;
    Integer voucherId;

    @NotBlank(message = "Vui lòng nhập tên người nhận.")
    String receiverName;

    @PhoneNumber(message = "Số điện thoại không hợp lệ.")
    String phoneNumber;

    @Min(value = 0, message = "Original amount is not valid.")
    BigDecimal originalAmount;

    @Min(value = 0, message = "Discount amount is not valid.")
    BigDecimal discountAmount;

    @Min(value = 0, message = "Shipping fee is not valid (value >= 0).")
    BigDecimal shippingFee;

    @Min(value = 0, message = "Discount amount is not valid.")
    BigDecimal totalAmount;

    @EnumValue(enumClass = OrderChannel.class, name = "OrderChannel", message = "Invalid value for Order channel")
    String orderChannel;

    @EnumValue(enumClass = OrderType.class, name = "OrderType", message = "Invalid value for Order type")
    String orderType;

    @EnumValue(enumClass = OrderStatus.class, name = "OrderStatus", message = "Invalid value for Order status")
    String orderStatus;

    @EnumValue(enumClass = PaymentMethod.class, name = "PaymentMethod", message = "Phương thức thanh toán không hợp lệ.")
    String paymentMethod;

    Boolean isPrepaid;

    Long shippingAddressId;

    String shippingAddress;

    String note;

    List<CreateOrderItemOnlineRequest> orderItems = new ArrayList<>();
}
