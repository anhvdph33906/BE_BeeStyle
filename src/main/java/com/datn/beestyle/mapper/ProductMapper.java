package com.datn.beestyle.mapper;

import com.datn.beestyle.common.IGenericMapper;
import com.datn.beestyle.dto.product.CreateProductRequest;
import com.datn.beestyle.dto.product.ProductResponse;
import com.datn.beestyle.dto.product.UpdateProductRequest;
import com.datn.beestyle.entity.product.Product;
import com.datn.beestyle.enums.GenderProduct;
import com.datn.beestyle.enums.Status;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ProductMapper extends IGenericMapper<Product, CreateProductRequest, UpdateProductRequest, ProductResponse> {

    @Mapping(target = "status", source = ".", qualifiedByName = "statusName")
    @Mapping(target = "genderProduct", source = ".", qualifiedByName = "genderProductName")
    @Mapping(target = "materialId", source = "material.id")
    @Mapping(target = "materialName", source = "material.materialName")
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.categoryName")
    @Mapping(target = "brandId", source = "brand.id")
    @Mapping(target = "brandName", source = "brand.brandName")
    @Override
    ProductResponse toEntityDto(Product entity);


    @Mapping(target = "gender", source = ".", qualifiedByName = "genderProductIdCreate")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productImages", ignore = true)
    @Mapping(target = "productVariants", ignore = true)
    @Mapping(target = "status", constant = "1")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Override
    Product toCreateEntity(CreateProductRequest request);

    @Mapping(target = "gender", source = ".", qualifiedByName = "genderProductIdUpdate")
    @Mapping(target = "status", source = ".", qualifiedByName = "statusId")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productImages", ignore = true)
    @Mapping(target = "productVariants", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Override
    void toUpdateEntity(@MappingTarget Product entity, UpdateProductRequest request);

    @Named("statusId")
    default int statusId(UpdateProductRequest request) {
        return Status.valueOf(request.getStatus()).getValue();
    }

    @Named("genderProductIdUpdate")
    default int genderProductIdUpdate(UpdateProductRequest request) {
        return GenderProduct.valueOf(request.getGenderProduct()).getValue();
    }

    @Named("genderProductIdCreate")
    default int genderProductIdCreate(CreateProductRequest request) {
        return GenderProduct.valueOf(request.getGenderProduct()).getValue();
    }

    @Named("statusName")
    default String statusName(Product product) {
        Status status = Status.resolve(product.getStatus());
        return status != null ? status.name() : null;
    }

    @Named("genderProductName")
    default String genderName(Product product) {
        GenderProduct genderProduct = GenderProduct.resolve(product.getStatus());
        return genderProduct != null ? genderProduct.name() : null;
    }
}
