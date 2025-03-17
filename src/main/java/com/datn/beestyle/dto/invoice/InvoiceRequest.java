package com.datn.beestyle.dto.invoice;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InvoiceRequest {
    private Long orderId;
    private String customerName;
    private String orderDate;
    private String paymentMethod;
    private List<Product> products;

    @Getter
    @Setter
    public static class Product {
        private String productName;
        private int quantity;
        private double price;
    }
}
