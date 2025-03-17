package com.datn.beestyle.service.voucher;

import com.datn.beestyle.common.IGenericService;
import com.datn.beestyle.dto.PageResponse;

import com.datn.beestyle.dto.voucher.CreateVoucherRequest;
import com.datn.beestyle.dto.voucher.UpdateVoucherRequest;
import com.datn.beestyle.dto.voucher.VoucherResponse;
import com.datn.beestyle.entity.Voucher;
import com.datn.beestyle.enums.DiscountType;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public interface IVoucherService
        extends IGenericService<Voucher, Integer, CreateVoucherRequest, UpdateVoucherRequest, VoucherResponse> {
    PageResponse<?> getAllByNameAndStatus(Pageable pageable, String name, String status, String discountType, Timestamp startDate, Timestamp endDate);

    List<VoucherResponse> createVoucher(List<CreateVoucherRequest> requestList);
}
