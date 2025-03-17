package com.datn.beestyle.mapper;

import com.datn.beestyle.common.IGenericMapper;
import com.datn.beestyle.dto.product.attributes.color.ColorResponse;
import com.datn.beestyle.dto.product.attributes.color.CreateColorRequest;
import com.datn.beestyle.dto.product.attributes.color.UpdateColorRequest;
import com.datn.beestyle.entity.product.attributes.Color;
import com.datn.beestyle.enums.Status;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ColorMapper extends IGenericMapper<Color, CreateColorRequest, UpdateColorRequest, ColorResponse> {

    @Mapping(target = "status", source = ".", qualifiedByName = "statusName")
    @Override
    ColorResponse toEntityDto(Color entity);

    @Mapping(target = "status", constant = "1")
    @Mapping(target = "colorCode", source = "colorCode", defaultValue = "default")
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Override
    Color toCreateEntity(CreateColorRequest request);

    @Mapping(target = "status", source = ".", qualifiedByName = "statusId")
    @Mapping(target = "colorCode", source = "colorCode", defaultValue = "default")
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Override
    void toUpdateEntity(@MappingTarget Color entity, UpdateColorRequest request);

    @Mapping(target = "colorCode", source = "colorCode", defaultValue = "default")
    @Mapping(target = "status", source = ".", qualifiedByName = "statusId")
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Color toUpdateEntity(UpdateColorRequest request);

    @Named("statusId")
    default int statusId(UpdateColorRequest request) {
        return Status.valueOf(request.getStatus()).getValue();
    }

    @Named("statusName")
    default String statusName(Color color) {
        return Status.valueOf(color.getStatus()).name();
    }
}