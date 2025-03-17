package com.datn.beestyle.dto.order;

import com.datn.beestyle.dto.address.AddressResponse;
import com.datn.beestyle.dto.customer.CustomerResponse;
import com.datn.beestyle.dto.voucher.VoucherResponse;
import com.datn.beestyle.enums.OrderChannel;
import com.datn.beestyle.enums.OrderStatus;
import com.datn.beestyle.enums.OrderType;
import com.datn.beestyle.enums.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderResponse {
    Long id;
    String orderTrackingNumber;
    Long customerId;
    String customerName;
    String phoneNumberCustomer;
    CustomerResponse customerInfo;
    Integer voucherId;
    VoucherResponse voucherInfo;
    Long shippingAddressId;
    AddressResponse shippingAddress;
    String phoneNumber;
    String receiverName;
    BigDecimal shippingFee;
    BigDecimal totalAmount;
    Timestamp paymentDate;
    String paymentMethod;
    Boolean isPrepaid;
    String orderChannel;
    String orderType;
    String orderStatus;
    String note;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    Long createdBy;
    Long updatedBy;

    public OrderResponse(Long id, String orderTrackingNumber, Long customerId, String customerName, String phoneNumberCustomer,
                         String receiverName, String phoneNumber, BigDecimal totalAmount, Timestamp paymentDate,
                         Integer paymentMethod, Integer orderChannel, Integer orderType, Integer orderStatus,
                         LocalDateTime createdAt, LocalDateTime updatedAt, Long createdBy, Long updatedBy) {
        this.id = id;
        this.orderTrackingNumber = orderTrackingNumber;
        this.customerId = customerId;
        this.customerName = customerName;
        this.phoneNumberCustomer = phoneNumberCustomer;
        this.receiverName = receiverName;
        this.phoneNumber = phoneNumber;
        this.totalAmount = totalAmount;
        this.paymentDate = paymentDate;
        this.paymentMethod = PaymentMethod.fromInteger(paymentMethod);
        this.orderChannel = OrderChannel.fromInteger(orderChannel);
        this.orderType = OrderType.fromInteger(orderType);
        this.orderStatus = OrderStatus.fromInteger(orderStatus);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }

    public OrderResponse(Long id, String orderTrackingNumber, Integer orderStatus, Integer orderChannel, Integer orderType,
                          BigDecimal totalAmount, LocalDateTime createdAt) {
        this.id = id;
        this.orderTrackingNumber = orderTrackingNumber;
        this.orderStatus = OrderStatus.fromInteger(orderStatus);
        this.orderChannel = OrderChannel.fromInteger(orderChannel);
        this.orderType = OrderType.fromInteger(orderType);
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
    }

    public OrderResponse(Long id, String orderTrackingNumber, Long customerId, Integer orderChannel, Integer orderType,
                         Integer orderStatus) {
        this.id = id;
        this.orderTrackingNumber = orderTrackingNumber;
        this.customerId = customerId;
        this.orderChannel = OrderChannel.fromInteger(orderChannel);
        this.orderType = OrderType.fromInteger(orderType);
        this.orderStatus = OrderStatus.fromInteger(orderStatus);
    }


}
