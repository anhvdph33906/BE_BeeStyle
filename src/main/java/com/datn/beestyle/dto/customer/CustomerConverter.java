package com.datn.beestyle.dto.customer;

public class CustomerConverter {
    public static CreateCustomerRequest toCreateCustomerRequest(RegisterCustomerRequest registerRequest) {
        CreateCustomerRequest createRequest = new CreateCustomerRequest();
        createRequest.setFullName(registerRequest.getFullName());
        createRequest.setDateOfBirth(registerRequest.getDateOfBirth());
        createRequest.setGender(registerRequest.getGender());
        createRequest.setPhoneNumber(registerRequest.getPhoneNumber());
        createRequest.setEmail(registerRequest.getEmail());
        createRequest.setAddresses(registerRequest.getAddresses());
        createRequest.setPassword(registerRequest.getPassword());
        // Không sao chép mật khẩu vì sẽ xử lý mật khẩu trong service
        return createRequest;
    }
}
