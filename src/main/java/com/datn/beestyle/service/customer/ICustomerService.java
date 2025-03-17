package com.datn.beestyle.service.customer;

import com.datn.beestyle.common.IGenericService;
import com.datn.beestyle.dto.PageResponse;
import com.datn.beestyle.dto.customer.*;
import com.datn.beestyle.entity.user.Customer;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICustomerService
        extends IGenericService<Customer, Long, CreateCustomerRequest, UpdateCustomerRequest, CustomerResponse> {
    PageResponse<?> getAllByKeywordAndStatusAndGender(Pageable pageable, String status, String gender, String keyword);

    Customer getCustomerByEmail(String email);

    CustomerResponse createByOwner(RegisterCustomerRequest request);

    CustomerResponse changePasswordByOwner(ChangePasswordCustomerRequest request);

    PageResponse<List<CustomerResponse>> ProductSalesByUser(Pageable pageable, Long id);
}
