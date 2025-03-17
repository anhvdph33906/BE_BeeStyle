package com.datn.beestyle.dto.staff;


import com.datn.beestyle.validation.PhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateStaffRequest {

    @NotBlank(message = "Không được để trống họ tên")
    String fullName;

    @NotBlank(message = "Không được để trống username")
    String username;

    LocalDate dateOfBirth;

    String gender;


    @NotBlank(message = "Không được để trống số điện thoại")
    @PhoneNumber(message = "Số điện thoại không đúng định dạng")
    String phoneNumber;



    @NotBlank(message = "Không được để trống email")
    @Email(message = "Email không hợp lệ")
    String email;

    String avatar;

    String address;

    @Size(min = 5, max = 10, message = "Password phải có độ dài từ {min} đến {max} ký tự")
    String password;


}
