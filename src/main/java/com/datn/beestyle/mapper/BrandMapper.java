package com.datn.beestyle.mapper;

import com.datn.beestyle.common.IGenericMapper;
import com.datn.beestyle.dto.brand.BrandResponse;
import com.datn.beestyle.dto.brand.CreateBrandRequest;
import com.datn.beestyle.dto.brand.UpdateBrandRequest;
import com.datn.beestyle.entity.product.attributes.Brand;
import com.datn.beestyle.enums.Status;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface BrandMapper extends IGenericMapper<Brand, CreateBrandRequest, UpdateBrandRequest, BrandResponse> {

    @Mapping(target = "status", source = ".", qualifiedByName = "statusName")
    @Override
    BrandResponse toEntityDto(Brand entity);

    @Mapping(target = "status", constant = "1")
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Override
    Brand toCreateEntity(CreateBrandRequest request);

    @Mapping(target = "status", source = ".", qualifiedByName = "statusId")
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Override
    void toUpdateEntity(@MappingTarget Brand entity, UpdateBrandRequest request);

    @Mapping(target = "status", source = ".", qualifiedByName = "statusId")
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Brand toUpdateEntity(UpdateBrandRequest request);

    @Named("statusId")
    default int statusId(UpdateBrandRequest request) {
        return Status.valueOf(request.getStatus()).getValue();
    }

    @Named("statusName")
    default String statusName(Brand brand) {
        return Status.valueOf(brand.getStatus()).name();
    }
}
