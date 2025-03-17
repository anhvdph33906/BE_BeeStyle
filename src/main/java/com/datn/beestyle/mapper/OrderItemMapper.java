package com.datn.beestyle.mapper;

import com.datn.beestyle.common.IGenericMapper;
import com.datn.beestyle.dto.order.item.CreateOrderItemRequest;
import com.datn.beestyle.dto.order.item.OrderItemResponse;
import com.datn.beestyle.dto.order.item.UpdateOrderItemRequest;
import com.datn.beestyle.entity.order.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrderItemMapper extends IGenericMapper<OrderItem, CreateOrderItemRequest, UpdateOrderItemRequest, OrderItemResponse> {

    @Mapping(target = "productVariantId", source = "productVariant.id")
    @Mapping(target = "orderId", source = "order.id")
    @Mapping(target = "sku", ignore = true)
    @Mapping(target = "productId", ignore = true)
    @Mapping(target = "productName", ignore = true)
    @Mapping(target = "colorId", ignore = true)
    @Mapping(target = "colorCode", ignore = true)
    @Mapping(target = "colorName", ignore = true)
    @Mapping(target = "sizeId", ignore = true)
    @Mapping(target = "sizeName", ignore = true)
    @Override
    OrderItemResponse toEntityDto(OrderItem entity);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "productVariant", ignore = true)
    OrderItem toCreateEntity(CreateOrderItemRequest request);

    @Override
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "productVariant", ignore = true)
    void toUpdateEntity(@MappingTarget OrderItem entity, UpdateOrderItemRequest request);
}
