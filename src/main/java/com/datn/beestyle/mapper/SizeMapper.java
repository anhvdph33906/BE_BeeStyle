package com.datn.beestyle.mapper;

import com.datn.beestyle.common.IGenericMapper;
import com.datn.beestyle.dto.product.attributes.size.CreateSizeRequest;
import com.datn.beestyle.dto.product.attributes.size.SizeResponse;
import com.datn.beestyle.dto.product.attributes.size.UpdateSizeRequest;
import com.datn.beestyle.entity.product.attributes.Size;
import com.datn.beestyle.enums.Status;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface SizeMapper extends IGenericMapper<Size, CreateSizeRequest, UpdateSizeRequest, SizeResponse> {

    @Mapping(target = "status", source = ".", qualifiedByName = "statusName")
    @Override
    SizeResponse toEntityDto(Size entity);

    @Mapping(target = "status", source = "status", defaultValue = "1")
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Override
    Size toCreateEntity(CreateSizeRequest request);

    @Mapping(target = "status", source = ".", qualifiedByName = "statusId")
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Override
    void toUpdateEntity(@MappingTarget Size entity, UpdateSizeRequest request);

    @Mapping(target = "status", source = ".", qualifiedByName = "statusId")
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Size toUpdateEntity(UpdateSizeRequest request);

    @Named("statusId")
    default int statusId(UpdateSizeRequest request) {
        return Status.valueOf(request.getStatus()).getValue();
    }

    @Named("statusName")
    default String statusName(Size size) {
        return Status.valueOf(size.getStatus()).name();
    }
}
