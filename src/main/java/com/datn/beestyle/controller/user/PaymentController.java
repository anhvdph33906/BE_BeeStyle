package com.datn.beestyle.controller.user;

import com.datn.beestyle.dto.ApiResponse;
import com.datn.beestyle.dto.order.CreateOrderOnlineRequest;
import com.datn.beestyle.service.order.IOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final IOrderService orderService;

    @PostMapping("/checkout")
    public ApiResponse<?> checkout(@Valid @RequestBody CreateOrderOnlineRequest request) {
        return new ApiResponse<>(HttpStatus.OK.value(), "Order online created successfully.",
                this.orderService.createOrderOnline(request));
    }
}
