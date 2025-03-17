package com.datn.beestyle.mapper;

import com.datn.beestyle.common.IGenericMapper;
import com.datn.beestyle.dto.material.CreateMaterialRequest;
import com.datn.beestyle.dto.material.MaterialResponse;
import com.datn.beestyle.dto.material.UpdateMaterialRequest;
import com.datn.beestyle.entity.product.Product;
import com.datn.beestyle.entity.product.attributes.Material;
import com.datn.beestyle.enums.Status;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface MaterialMapper extends IGenericMapper<Material, CreateMaterialRequest, UpdateMaterialRequest, MaterialResponse> {

    @Mapping(target = "status", source = ".", qualifiedByName = "statusName")
    @Override
    MaterialResponse toEntityDto(Material entity);

    @Mapping(target = "status", constant = "1")
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Override
    Material toCreateEntity(CreateMaterialRequest request);

    @Mapping(target = "status", source = ".", qualifiedByName = "statusId")
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Override
    void toUpdateEntity(@MappingTarget Material entity, UpdateMaterialRequest request);

    @Mapping(target = "status", source = ".", qualifiedByName = "statusId")
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Material toUpdateEntity(UpdateMaterialRequest request);

    @Named("statusId")
    default int statusId(UpdateMaterialRequest request) {
        return Status.valueOf(request.getStatus()).getValue();
    }

    @Named("statusName")
    default String statusName(Material material) {
        Status status = Status.resolve(material.getStatus());
        return status != null ? status.name() : null;
    }
}
