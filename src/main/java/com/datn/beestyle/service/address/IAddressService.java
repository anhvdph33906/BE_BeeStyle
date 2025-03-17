package com.datn.beestyle.service.address;

import com.datn.beestyle.common.IGenericService;
import com.datn.beestyle.dto.PageResponse;
import com.datn.beestyle.dto.address.AddressResponse;
import com.datn.beestyle.dto.address.CreateAddressRequest;
import com.datn.beestyle.dto.address.UpdateAddressRequest;
import com.datn.beestyle.entity.Address;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface IAddressService extends
        IGenericService<Address, Long, CreateAddressRequest, UpdateAddressRequest, AddressResponse> {

    @Transactional
    AddressResponse setUpdateIsDefault(Long id, UpdateAddressRequest request);

    PageResponse<?> getAllByCustomerId(Pageable pageable, Long customerId);


}
