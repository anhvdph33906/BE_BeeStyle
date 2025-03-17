package com.datn.beestyle.controller;

import com.datn.beestyle.dto.ApiResponse;
import com.datn.beestyle.dto.order.item.CreateOrderItemRequest;
import com.datn.beestyle.dto.order.item.PatchUpdateQuantityOrderItem;
import com.datn.beestyle.dto.order.item.UpdateOrderItemRequest;
import com.datn.beestyle.service.order.item.IOrderItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping(path = "/admin")
@RequiredArgsConstructor
public class OrderItemController {

    private final IOrderItemService orderItemService;

    @GetMapping("/order/{orderId}/order-items")
    public ApiResponse<?> getOrderItemsByOrderId(@PathVariable("orderId") Long orderId) {
        return new ApiResponse<>(HttpStatus.OK.value(), "Order items", orderItemService.getAllByOrderId(orderId));
    }

    @PostMapping("/order-item/create")
    public ApiResponse<?> createOrderItemByOrderId(@RequestBody @Valid CreateOrderItemRequest request) {
        return new ApiResponse<>(HttpStatus.OK.value(), "Create Order item", orderItemService.create(request));
    }

    @PostMapping("/order-item/creates")
    public ApiResponse<?> createsOrderItemByOrderId(@RequestParam Long orderId,
                                                    @RequestBody List<@Valid UpdateOrderItemRequest> request) {
        return new ApiResponse<>(HttpStatus.OK.value(), "Creates Order item",
                orderItemService.createOrUpdateOrderItems(orderId, request));
    }

    @PostMapping("/order-item/creates/delivery-sale")
    public ApiResponse<?> createsOrderItemByOrderIdForDeliverySale(@RequestParam Long orderId,
                                                                   @RequestBody List<@Valid UpdateOrderItemRequest> request) {
        return new ApiResponse<>(HttpStatus.OK.value(), "Creates order item delivery sale",
                orderItemService.createOrUpdateOrderItemsDeliverySale(orderId, request));
    }

    @PutMapping("/order-item/{orderItemId}/update")
    public ApiResponse<?> updateOrderItemByOrderId(@Valid @RequestBody UpdateOrderItemRequest request,
                                                   @PathVariable("orderItemId") Long orderItemId) {
        return new ApiResponse<>(HttpStatus.OK.value(), "Update Order item", orderItemService.update(orderItemId, request));
    }

    @PatchMapping("/order-item/update-quantity")
    public ApiResponse<?> patchQuantityOrderItemByOrderId(@Valid @RequestBody PatchUpdateQuantityOrderItem request) {
        return new ApiResponse<>(HttpStatus.OK.value(), "Patch update Order item",
                orderItemService.patchUpdateQuantity(request));
    }

    @DeleteMapping("/order-item/{orderItemId}/delete")
    public ApiResponse<?> deleteOrderItemByOrderId(@PathVariable("orderItemId") Long orderItemId) {
        orderItemService.delete(orderItemId);
        return new ApiResponse<>(HttpStatus.OK.value(), "Deleted order item");
    }
}
