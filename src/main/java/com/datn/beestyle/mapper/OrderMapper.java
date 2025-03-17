package com.datn.beestyle.mapper;

import com.datn.beestyle.common.IGenericMapper;
import com.datn.beestyle.dto.material.UpdateMaterialRequest;
import com.datn.beestyle.dto.order.CreateOrderRequest;
import com.datn.beestyle.dto.order.OrderResponse;
import com.datn.beestyle.dto.order.UpdateOrderRequest;
import com.datn.beestyle.entity.order.Order;
import com.datn.beestyle.entity.product.attributes.Material;
import com.datn.beestyle.enums.*;
import org.mapstruct.*;

import java.util.List;
@Mapper(componentModel = "spring")
public interface OrderMapper extends IGenericMapper<Order, CreateOrderRequest, UpdateOrderRequest, OrderResponse> {

    @Override
    @Mapping(target = "customerName", ignore = true)
    @Mapping(target = "customerInfo", ignore = true)
    @Mapping(target = "voucherInfo", ignore = true)
    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "voucherId", source = "voucher.id")
    @Mapping(target = "shippingAddressId", source = "shippingAddress.id")
    @Mapping(target = "paymentMethod", source = ".", qualifiedByName = "paymentMethodName")
    @Mapping(target = "orderChannel", source = ".", qualifiedByName = "orderChannelName")
    @Mapping(target = "orderType", source = ".", qualifiedByName = "orderTypeName")
    @Mapping(target = "orderStatus", source = ".", qualifiedByName = "orderStatusName")
    OrderResponse toEntityDto(Order entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderTrackingNumber", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "shippingFee", constant = "0")
    @Mapping(target = "totalAmount", constant = "0")
    @Mapping(target = "orderChannel", source = ".", qualifiedByName = "orderChannelIdCreate")
    @Mapping(target = "orderStatus", source = ".", qualifiedByName = "orderStatusIdCreate")
    @Mapping(target = "orderType", source = ".", qualifiedByName = "orderTypeIdCreate")
    @Override
    Order toCreateEntity(CreateOrderRequest orderRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "shippingAddress", ignore = true)
    @Mapping(target = "orderChannel", source = ".", qualifiedByName = "orderChannelIdUpdate")
    @Mapping(target = "orderStatus", source = ".", qualifiedByName = "orderStatusIdUpdate")
    @Mapping(target = "paymentMethod", source = ".", qualifiedByName = "paymentMethodIdUpdate")
    void toUpdateEntity(@MappingTarget Order entity, UpdateOrderRequest request);

    @Mapping(target = "shippingAddress", ignore = true)
    @Override
    List<Order> toUpdateEntityList(List<UpdateOrderRequest> dtoUpdateList);

    @Named("orderStatusIdCreate")
    default int orderStatusIdCreate(CreateOrderRequest request) {
        return OrderStatus.valueOf(request.getOrderStatus()).getValue();
    }

    @Named("orderChannelIdCreate")
    default int orderChannelIdCreate(CreateOrderRequest request) {
        return OrderChannel.valueOf(request.getOrderChannel()).getValue();
    }

    @Named("orderTypeIdCreate")
    default int orderTypeIdCreate(CreateOrderRequest request) {
        return OrderType.valueOf(request.getOrderType()).getValue();
    }

    @Named("orderStatusIdUpdate")
    default int orderStatusIdUpdate(UpdateOrderRequest request) {
        return OrderStatus.valueOf(request.getOrderStatus()).getValue();
    }

    @Named("orderChannelIdUpdate")
    default int orderChannelIdUpdate(UpdateOrderRequest request) {
        return OrderChannel.valueOf(request.getOrderChannel()).getValue();
    }

    @Named("paymentMethodIdUpdate")
    default int paymentMethodIdUpdate(UpdateOrderRequest request) {
        return PaymentMethod.valueOf(request.getPaymentMethod()).getValue();
    }

    @Named("paymentMethodName")
    default String paymentMethodName(Order order) {
        PaymentMethod paymentMethod = PaymentMethod.resolve(order.getPaymentMethod());
        return paymentMethod != null ? paymentMethod.name() : null;
    }

    @Named("orderChannelName")
    default String orderChannelName(Order order) {
        OrderChannel orderChannel = OrderChannel.resolve(order.getOrderChannel());
        return orderChannel != null ? orderChannel.name() : null;
    }

    @Named("orderTypeName")
    default String orderTypeName(Order order) {
        OrderType orderType = OrderType.resolve(order.getOrderType());
        return orderType != null ? orderType.name() : null;
    }

    @Named("orderStatusName")
    default String orderStatusName(Order order) {
        OrderStatus orderStatus = OrderStatus.resolve(order.getOrderStatus());
        return orderStatus != null ? orderStatus.name() : null;
    }
}

