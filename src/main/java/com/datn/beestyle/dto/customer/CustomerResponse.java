package com.datn.beestyle.dto.customer;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerResponse {
    Long id;
    String fullName;
    LocalDate dateOfBirth;
    String gender;
    String phoneNumber;
    String email;
    String role;
    String address;
    String status;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;


    String productName;
    BigDecimal salePrice;
    Integer totalQuantity;
    String imageProduct;

    public CustomerResponse(String productName,BigDecimal salePrice,Integer totalQuantity,String imageProduct){
        this.productName = productName;
        this.salePrice = salePrice;
        this.totalQuantity = totalQuantity;
        this.imageProduct = imageProduct;

    }
}