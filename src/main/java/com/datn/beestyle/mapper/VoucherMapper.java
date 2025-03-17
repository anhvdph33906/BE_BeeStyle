package com.datn.beestyle.mapper;

import com.datn.beestyle.common.IGenericMapper;
import com.datn.beestyle.dto.promotion.CreatePromotionRequest;
import com.datn.beestyle.dto.voucher.CreateVoucherRequest;
import com.datn.beestyle.dto.voucher.UpdateVoucherRequest;
import com.datn.beestyle.dto.voucher.VoucherResponse;
import com.datn.beestyle.entity.Voucher;
import com.datn.beestyle.enums.DiscountStatus;
import com.datn.beestyle.enums.DiscountType;
import com.datn.beestyle.enums.Status;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.sql.Timestamp;
import java.util.List;
@Mapper(componentModel = "spring")
public interface VoucherMapper extends IGenericMapper<Voucher, CreateVoucherRequest, UpdateVoucherRequest, VoucherResponse> {

    @Mapping(target = "status", source = ".", qualifiedByName = "statusName")
    @Mapping(target = "discountType", source = "discountType", qualifiedByName = "discountTypeName")
    @Override
    VoucherResponse toEntityDto(Voucher entity);

    @Mapping(target = "status", source = "request", qualifiedByName = "determineStatusForCreate")
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "discountType", source = "discountType", qualifiedByName = "discountTypeId")
    @Override
    Voucher toCreateEntity(CreateVoucherRequest request);

    @Mapping(target = "status", source = "request", qualifiedByName = "determineStatusForUpdate")
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "discountType", source = "discountType", qualifiedByName = "discountTypeId")
    @Override
    void toUpdateEntity(@MappingTarget Voucher entity, UpdateVoucherRequest request);

    @Override
    List<VoucherResponse> toEntityDtoList(List<Voucher> entityList);

    @Named("statusId")
    default int statusId(UpdateVoucherRequest request) {
        return DiscountStatus.valueOf(request.getStatus()).getValue();
    }

    @Named("statusName")
    default String statusName(Voucher voucher) {
        DiscountStatus status = DiscountStatus.resolve(voucher.getStatus());
        return status != null ? status.name() : null;
    }

    @Named("discountTypeName")
    default String discountTypeName(int discountType) {
        DiscountType type = DiscountType.resolve(discountType);
        return type != null ? type.name() : null;
    }

    @Named("discountTypeId")
    default Integer discountTypeId(String discountType) {
        DiscountType type = DiscountType.fromString(discountType);
        return type != null ? type.getValue() : null;
    }
    @Named("determineStatusForCreate")
    default int determineStatusForCreate(CreateVoucherRequest request) {
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
    default int determineStatusForUpdate(UpdateVoucherRequest request) {
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
