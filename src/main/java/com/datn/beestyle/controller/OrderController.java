package com.datn.beestyle.controller;

import com.datn.beestyle.dto.ApiResponse;
import com.datn.beestyle.dto.order.CreateOrderRequest;
import com.datn.beestyle.dto.order.UpdateOrderRequest;
import com.datn.beestyle.dto.order.UpdateOrderStatusDeliveryRequest;
import com.datn.beestyle.service.order.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Validated
@RestController
@RequestMapping(path = "/admin/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ApiResponse<?> getOrders(Pageable pageable, @RequestParam(required = false) Map<String, String> filters) {
        return new ApiResponse<>(HttpStatus.OK.value(), "Orders",
                this.orderService.getOrdersFilterByFields(pageable, filters));
    }

    @GetMapping("/order-pending")
    public ApiResponse<?> getOrdersPending() {
        return new ApiResponse<>(HttpStatus.OK.value(), "Orders Pending",
                this.orderService.getOrdersPending());
    }

    @GetMapping("/{orderId}")
    public ApiResponse<?> getOrderDetail(@PathVariable("orderId") Long orderId) {
        return new ApiResponse<>(HttpStatus.OK.value(), "Order detail",
                orderService.getOrderDetailById(orderId));
    }

    @PostMapping("/create")
    public ApiResponse<?> createOrder(@Valid  @RequestBody CreateOrderRequest request) {
        return new ApiResponse<>(HttpStatus.OK.value(), "Order pending added successfully.",
                orderService.create(request));
    }

    @PostMapping("/update/{orderId}")
    public ApiResponse<?> updateOrder(@Min(1) @PathVariable("orderId") Long orderId,
                                      @Valid @RequestBody UpdateOrderRequest request) {
        return new ApiResponse<>(HttpStatus.OK.value(), "Order updated successfully.",
                orderService.update(orderId, request));
    }

    @PostMapping("/{orderId}/update-status")
    public ApiResponse<?> updateOrderStatus(@Min(1) @PathVariable("orderId") Long orderId,
                                            @Valid @RequestBody UpdateOrderStatusDeliveryRequest request) {
        return new ApiResponse<>(HttpStatus.OK.value(), "Order status updated successfully.",
                orderService.updateOrderOnline(orderId, request));
    }
}
