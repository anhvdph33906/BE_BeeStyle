package com.datn.beestyle.security.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignInRequest { // đăng nhập
    @NotBlank(message = "Tài khoản không được để trống.")
    private String username;

    @Size(min = 5, max = 10,message = "Mật khẩu phải có độ dài từ 5 đến 10 ký tự.")
    private String password;
}
