package com.datn.beestyle.mapper;

import com.datn.beestyle.common.IGenericMapper;
import com.datn.beestyle.dto.product.variant.CreateProductVariantRequest;
import com.datn.beestyle.dto.product.variant.ProductVariantResponse;
import com.datn.beestyle.dto.product.variant.UpdateProductVariantRequest;
import com.datn.beestyle.dto.voucher.UpdateVoucherRequest;
import com.datn.beestyle.entity.Voucher;
import com.datn.beestyle.entity.product.ProductVariant;
import com.datn.beestyle.enums.Status;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ProductVariantMapper
        extends IGenericMapper<ProductVariant, CreateProductVariantRequest, UpdateProductVariantRequest, ProductVariantResponse> {

    @Mapping(target = "status", constant = "1")
    @Override
    ProductVariant toCreateEntity(CreateProductVariantRequest request);


    @Mapping(target = "status", source = ".", qualifiedByName = "statusId")
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Override
    void toUpdateEntity(@MappingTarget ProductVariant entity, UpdateProductVariantRequest request);


//    @Mapping(target = "productName", ignore = true)
//    @Mapping(target = "colorName", ignore = true)
//    @Mapping(target = "sizeName", ignore = true)
//    void updateProductVariantFromRequest(UpdateProductVariantRequest request, @MappingTarget ProductVariant entity);
//

    @Named("statusId")
    default int statusId(UpdateProductVariantRequest request) {
        return Status.valueOf(request.getStatus()).getValue();
    }

    @Named("statusName")
    default String statusName(Voucher voucher) {
        Status status = Status.resolve(voucher.getStatus());
        return status != null ? status.name() : null;
    }
}
