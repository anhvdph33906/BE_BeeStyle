package com.datn.beestyle.controller.user;

import com.datn.beestyle.dto.ApiResponse;
import com.datn.beestyle.service.order.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
public class OrderByUserController {

    private final IOrderService orderService;

    @GetMapping("/orders-by-customer")
    public ApiResponse<?> ordersTrackingByCustomer(@RequestParam("customerId") Long customerId,
                                                  Pageable pageable) {
        return new ApiResponse<>(HttpStatus.OK.value(), "Order by customer.",
                this.orderService.getOrderByCustomerId(customerId, pageable));
    }

    @GetMapping("/order-by-customer/{orderTrackingNumber}")
    public ApiResponse<?> getDetailOrderByOrderTrackingNumber(@PathVariable String orderTrackingNumber) {
        return new ApiResponse<>(HttpStatus.OK.value(), "Order detail by order tracking number.",
                this.orderService.getOrderDetailByOrderTrackingNumber(orderTrackingNumber));
    }
}
