package com.datn.beestyle.service.order;

import com.datn.beestyle.common.IGenericService;
import com.datn.beestyle.dto.PageResponse;
import com.datn.beestyle.dto.order.*;
import com.datn.beestyle.entity.order.Order;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface IOrderService
        extends IGenericService<Order, Long, CreateOrderRequest, UpdateOrderRequest, OrderResponse> {
    PageResponse<List<OrderResponse>> getOrdersFilterByFields(Pageable pageable, Map<String, String> filters);
    List<OrderResponse> getOrdersPending();
    OrderResponse getOrderDetailById(Long id);
    OrderResponse getOrderDetailByOrderTrackingNumber(String orderTrackingNumber);
    String updateOrderOnline(Long id, UpdateOrderStatusDeliveryRequest request);
    OrderResponse createOrderOnline(CreateOrderOnlineRequest request);
    PageResponse<List<OrderResponse>> getOrderByCustomerId(Long customerId, Pageable pageable);


}
