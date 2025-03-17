package com.datn.beestyle.dto.order;

import com.datn.beestyle.enums.OrderChannel;
import com.datn.beestyle.enums.OrderStatus;

import com.datn.beestyle.enums.OrderType;
import com.datn.beestyle.validation.EnumValue;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateOrderRequest {

    BigDecimal shippingFee;

    BigDecimal totalAmount;

    @EnumValue(enumClass = OrderChannel.class, name = "OrderChannel", message = "Invalid value for Order channel")
    String orderChannel;

    @EnumValue(enumClass = OrderType.class, name = "OrderType", message = "Invalid value for Order type")
    String orderType;

    @EnumValue(enumClass = OrderStatus.class, name = "OrderStatus", message = "Invalid value for Order status")
    String orderStatus;
}
