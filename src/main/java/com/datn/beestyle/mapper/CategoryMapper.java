package com.datn.beestyle.mapper;

import com.datn.beestyle.common.IGenericMapper;
import com.datn.beestyle.dto.category.CategoryResponse;
import com.datn.beestyle.dto.category.CreateCategoryRequest;
import com.datn.beestyle.dto.category.UpdateCategoryRequest;
import com.datn.beestyle.dto.product.attributes.color.UpdateColorRequest;
import com.datn.beestyle.entity.Category;
import com.datn.beestyle.entity.product.attributes.Color;
import com.datn.beestyle.enums.Status;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CategoryMapper 
        extends IGenericMapper<Category, CreateCategoryRequest, UpdateCategoryRequest, CategoryResponse> {

    @Mapping(target = "status", source = ".", qualifiedByName = "statusName")
    @Mapping(target = "parentCategoryId", source = "parentCategory.id")
    @Mapping(target = "parentCategoryName", ignore = true)
    @Override
    CategoryResponse toEntityDto(Category entity);

    @Mapping(target = "status", constant = "1")
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "parentCategory", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "categoryChildren", ignore = true)
    @Override
    Category toCreateEntity(CreateCategoryRequest request);

    @Mapping(target = "status", source = ".", qualifiedByName = "statusId")
    @Mapping(target = "level", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "categoryChildren", ignore = true)
    @Override
    void toUpdateEntity(@MappingTarget Category entity, UpdateCategoryRequest request);

    @Named("statusId")
    default int statusId(UpdateCategoryRequest request) {
        return Status.valueOf(request.getStatus()).getValue();
    }

    @Named("statusName")
    default String statusName(Category category) {
        return Status.valueOf(category.getStatus()).name();
    }

}
