package com.datn.beestyle.mapper;

import com.datn.beestyle.common.IGenericMapper;
import com.datn.beestyle.dto.promotion.CreatePromotionRequest;
import com.datn.beestyle.dto.promotion.PromotionResponse;
import com.datn.beestyle.dto.promotion.UpdatePromotionRequest;
import com.datn.beestyle.dto.voucher.CreateVoucherRequest;
import com.datn.beestyle.dto.voucher.UpdateVoucherRequest;
import com.datn.beestyle.entity.Promotion;
import com.datn.beestyle.entity.Voucher;
import com.datn.beestyle.enums.DiscountStatus;
import com.datn.beestyle.enums.DiscountType;
import com.datn.beestyle.enums.Status;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.sql.Timestamp;

@Mapper(componentModel = "spring")
public interface PromotionMapper extends IGenericMapper<Promotion, CreatePromotionRequest, UpdatePromotionRequest, PromotionResponse> {

    @Mapping(target = "status", source = ".", qualifiedByName = "statusName")
    @Mapping(target = "discountType", source = ".", qualifiedByName = "discountTypeName")
    @Override
    PromotionResponse toEntityDto(Promotion entity);

    @Mapping(target = "status", source = "request", qualifiedByName = "determineStatusForCreate")
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "discountType", source = "discountType", qualifiedByName = "discountTypeId")
    @Override
    Promotion toCreateEntity(CreatePromotionRequest request);

    @Mapping(target = "status", source = "request", qualifiedByName = "determineStatusForUpdate")
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "discountType", source = "discountType", qualifiedByName = "discountTypeId")
    @Override
    void toUpdateEntity(@MappingTarget Promotion entity, UpdatePromotionRequest request);

    @Named("statusName")
    default String statusName(Promotion promotion) {
        DiscountStatus status = DiscountStatus.resolve(promotion.getStatus());
        return status != null ? status.name() : null;
    }

    @Named("discountTypeName")
    default String discountTypeName(Promotion promotion) {
        DiscountType discountType = DiscountType.resolve(promotion.getDiscountType());
        return discountType != null ? discountType.name() : null;
    }
    @Named("discountTypeId")
    default Integer discountTypeId(String discountType) {
        DiscountType type = DiscountType.fromString(discountType);
        return type != null ? type.getValue() : null;
    }
    @Named("determineStatusForCreate")
    default int determineStatusForCreate(CreatePromotionRequest request) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        if (now.before(request.getStartDate())) {
            return 0;
        } else if (!now.after(request.getEndDate())) {
            return 1;
        } else {
            return 2;
        }
    }

    @Named("determineStatusForUpdate")
    default int determineStatusForUpdate(UpdatePromotionRequest request) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        if (now.before(request.getStartDate())) {
            return 0;
        } else if (!now.after(request.getEndDate())) {
            return 1;
        } else {
            return 2;
        }
    }
}
