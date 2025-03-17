package com.datn.beestyle.dto.order;

import com.datn.beestyle.enums.OrderStatus;
import com.datn.beestyle.validation.EnumValue;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateOrderStatusDeliveryRequest {

    BigDecimal shippingFee;

    BigDecimal totalAmount;

    @EnumValue(enumClass = OrderStatus.class, name = "OrderStatus", message = "Invalid value for order status")
    String orderStatus;

    String note;
}
