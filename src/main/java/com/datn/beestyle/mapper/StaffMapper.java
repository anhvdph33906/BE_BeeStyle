package com.datn.beestyle.mapper;

import com.datn.beestyle.common.IGenericMapper;
import com.datn.beestyle.dto.customer.UpdateCustomerRequest;
import com.datn.beestyle.dto.staff.CreateStaffRequest;
import com.datn.beestyle.dto.staff.StaffResponse;
import com.datn.beestyle.dto.staff.UpdateStaffRequest;
import com.datn.beestyle.entity.user.Customer;
import com.datn.beestyle.entity.user.Staff;
import com.datn.beestyle.enums.Gender;
import com.datn.beestyle.enums.Status;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface StaffMapper extends IGenericMapper<Staff, CreateStaffRequest, UpdateStaffRequest, StaffResponse> {
    @Mapping(target = "status", source = ".", qualifiedByName = "statusName")
    @Mapping(target = "gender", source = ".", qualifiedByName = "genderName")
    @Override
    StaffResponse toEntityDto(Staff entity);

    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "status",constant = "1")
    @Mapping(target = "role",expression = "java(com.datn.beestyle.enums.Role.USER)")
    @Override
    Staff toCreateEntity(CreateStaffRequest request);

    @Mapping(target = "status", source = ".", qualifiedByName = "statusId")
    @Mapping(target = "gender", source = ".", qualifiedByName = "genderId")
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Override
    void toUpdateEntity(@MappingTarget Staff entity, UpdateStaffRequest request);

    @Named("statusId")
    default int statusId(UpdateStaffRequest request) {
        return Status.valueOf(request.getStatus()).getValue();
    }

    @Named("statusName")
    default String statusName(Staff staff) {
        return Status.valueOf(staff.getStatus()).name();
    }

    @Named("genderName")
    default String genderName(Staff staff) {
        // Kiểm tra và ánh xạ số nguyên từ `gender` sang enum `Gender`
        return Gender.valueOf(staff.getGender()).name();
    }

    @Named("genderId")
    default int genderId(UpdateStaffRequest request) {
        return Gender.valueOf(request.getGender()).getValue();
    }
}
