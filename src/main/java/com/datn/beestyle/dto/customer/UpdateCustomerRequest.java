package com.datn.beestyle.dto.customer;

import com.datn.beestyle.entity.Address;
import com.datn.beestyle.validation.PhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateCustomerRequest {
    @NotBlank(message = "Không được để trống fullname")
    String fullName;

    LocalDate dateOfBirth;

    String gender;

    @NotBlank(message = "Không được để trống số điện thoại")
    @PhoneNumber(message = "Số điện thoại không đung")
    String phoneNumber;

    @NotBlank(message = "Không được để trống email")
    @Email(message = "Email không đúng định dạng")
    String email;

    @Size(min = 5, max = 10, message = "Password phải có độ dài từ {min} đến {max} ký tự")
    String password;

    String status;

//    ShoppingCart shoppingCart;

    Set<Address> addresses = new HashSet<>();
}