package com.datn.beestyle.mapper;

import com.datn.beestyle.common.IGenericMapper;
import com.datn.beestyle.dto.customer.CreateCustomerRequest;
import com.datn.beestyle.dto.customer.CustomerResponse;
import com.datn.beestyle.dto.customer.UpdateCustomerRequest;
import com.datn.beestyle.entity.user.Customer;
import com.datn.beestyle.enums.Gender;
import com.datn.beestyle.enums.Status;
import com.datn.beestyle.enums.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;


@Mapper(componentModel = "spring")
public interface CustomerMapper extends IGenericMapper<Customer, CreateCustomerRequest, UpdateCustomerRequest, CustomerResponse> {
    @Mapping(target = "status", source = ".", qualifiedByName = "statusName")
    @Mapping(target = "gender", source = ".", qualifiedByName = "genderName")
    @Override
    CustomerResponse toEntityDto(Customer entity);

    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "status", constant = "1")
    @Mapping(target = "role",expression = "java(com.datn.beestyle.enums.Role.OWNER)")
    @Mapping(target = "addresses", source = "addresses")
    @Override
    Customer toCreateEntity(CreateCustomerRequest request);

    @Mapping(target = "status", source = ".", qualifiedByName = "statusId")
    @Mapping(target = "gender", source = ".", qualifiedByName = "genderId")
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Override
    void toUpdateEntity(@MappingTarget Customer entity, UpdateCustomerRequest request);

    @Named("statusName")
    default String statusName(Customer customer) {
        return Status.valueOf(customer.getStatus()).name();
    }

    @Named("genderName")
    default String genderName(Customer customer) {
        return Gender.valueOf(customer.getGender()).name();
    }

    @Named("statusId")
    default int statusId(UpdateCustomerRequest request) {
        return Status.valueOf(request.getStatus()).getValue();
    }

    @Named("genderId")
    default int genderId(UpdateCustomerRequest request) {
        return Gender.valueOf(request.getGender()).getValue();
    }



}
