package com.datn.beestyle.security.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequest {
    private String secretKey;
    private String password;
    private String confirmPassword;
}
