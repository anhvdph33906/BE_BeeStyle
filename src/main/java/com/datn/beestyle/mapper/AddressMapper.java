package com.datn.beestyle.mapper;

import com.datn.beestyle.common.IGenericMapper;
import com.datn.beestyle.dto.address.AddressResponse;
import com.datn.beestyle.dto.address.CreateAddressRequest;
import com.datn.beestyle.dto.address.UpdateAddressRequest;
import com.datn.beestyle.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AddressMapper extends IGenericMapper<Address, CreateAddressRequest, UpdateAddressRequest, AddressResponse> {


    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Override
    Address toCreateEntity(CreateAddressRequest request);

    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Override
    void toUpdateEntity(@MappingTarget Address entity, UpdateAddressRequest request);


//    // Phương thức kiểm tra và thiết lập giá trị isDefault
//    default boolean checkAndSetDefault(CreateAddressRequest request) {
//        boolean existsDefaultAddress = addressRepository.existsByIsDefaultTrue();
//        return !existsDefaultAddress; // Nếu chưa có bản ghi nào có isDefault = true, trả về true
//    }
}
