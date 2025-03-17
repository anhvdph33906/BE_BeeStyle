package com.datn.beestyle.controller.user;


import com.datn.beestyle.service.ghtk.GHTKService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@CrossOrigin("*")
@RestController
@RequestMapping("/ghtk")
public class GHTKController {

    private final GHTKService ghtkService;

    public GHTKController(GHTKService ghtkService) {
        this.ghtkService = ghtkService;
    }

    @PostMapping("/calculate-fee")
    public ResponseEntity<String> calculateShippingFee(@RequestBody Map<String, Object> request) {
        // Gọi service để tính phí vận chuyển
        return ghtkService.calculateShippingFee(request);
    }
}