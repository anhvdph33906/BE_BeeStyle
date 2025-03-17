package com.datn.beestyle.service.order.item;

import com.datn.beestyle.common.IGenericService;
import com.datn.beestyle.dto.order.item.CreateOrderItemRequest;
import com.datn.beestyle.dto.order.item.OrderItemResponse;
import com.datn.beestyle.dto.order.item.PatchUpdateQuantityOrderItem;
import com.datn.beestyle.dto.order.item.UpdateOrderItemRequest;
import com.datn.beestyle.entity.order.OrderItem;

import java.util.List;
import java.util.Map;

public interface IOrderItemService
        extends IGenericService<OrderItem, Long, CreateOrderItemRequest, UpdateOrderItemRequest, OrderItemResponse> {
    List<OrderItemResponse> getAllByOrderId(Long orderId);
    Map<Long, Long> createOrUpdateOrderItems(Long orderId, List<UpdateOrderItemRequest> requests);
    Map<Long, Long> createOrUpdateOrderItemsDeliverySale(Long orderId, List<UpdateOrderItemRequest> requests);
    int patchUpdateQuantity(PatchUpdateQuantityOrderItem request);
}
