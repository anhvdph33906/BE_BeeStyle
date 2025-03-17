package com.datn.beestyle.controller;

import com.datn.beestyle.dto.ApiResponse;
import com.datn.beestyle.dto.voucher.CreateVoucherRequest;
import com.datn.beestyle.dto.voucher.UpdateVoucherRequest;
import com.datn.beestyle.dto.voucher.VoucherResponse;
import com.datn.beestyle.service.voucher.VoucherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/admin/voucher")
@RequiredArgsConstructor
@CrossOrigin("*")
public class VoucherController {
    private final VoucherService voucherService;
    @GetMapping
    public ApiResponse<?> getVouchers(Pageable pageable,
                                      @RequestParam(required = false) String name,
                                      @RequestParam(required = false) String status,
                                      @RequestParam(required = false) String discountType,
                                      @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                      @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Timestamp startTimestamp = null;
        Timestamp endTimestamp = null;

        if (startDate != null) {
            startTimestamp = Timestamp.valueOf(startDate.atStartOfDay());
        }

        if (endDate != null) {
            endTimestamp = Timestamp.valueOf(endDate.atTime(23, 59, 59));
        }

        return new ApiResponse<>(HttpStatus.OK.value(), "Vouchers",
                voucherService.getAllByNameAndStatus(pageable, name, status, discountType, startTimestamp, endTimestamp));
    }


    @GetMapping("/vouchers")
    public ApiResponse<?> getAllVouchers(Pageable pageable) {
        return new ApiResponse<>(HttpStatus.OK.value(), "Vouchers", voucherService.getAll(pageable));
    }

    @PostMapping("/create")
    public ApiResponse<?> createVoucher(@Valid @RequestBody CreateVoucherRequest request) {
        return new ApiResponse<>(HttpStatus.CREATED.value(), "Thêm mới voucher thành công!",
                voucherService.create(request));
    }

    @PutMapping("/update/{id}")
    public ApiResponse<?> updateVoucher(@PathVariable Integer id, @RequestBody UpdateVoucherRequest request) {
        return new ApiResponse<>(HttpStatus.CREATED.value(), "Cập nhật voucher thành công!",
                voucherService.update(id, request));
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<?> deleteVoucher(@PathVariable Integer id) {
        voucherService.delete(id);
        return new ApiResponse<>(HttpStatus.OK.value(), "Xóa voucher thành công");
    }

    @GetMapping("/{id}")
    public ApiResponse<?> getVoucher(@PathVariable Integer id) {
        return new ApiResponse<>(HttpStatus.OK.value(), "Voucher", voucherService.getDtoById(id));
    }

    //    @GetMapping("/voucherName/{voucherName}")
//    public ApiResponse<?> getVoucherByCode(@PathVariable String voucherName) {
//        return new ApiResponse<>(HttpStatus.OK.value(), "Voucher found", voucherService.getVoucherByName(voucherName));
//    }
//    @GetMapping("/search")
//    public ApiResponse<?> searchVouchers(
//            @RequestParam(required = false) String searchTerm,
//            Pageable pageable) {
//        Page<VoucherResponse> vouchers = voucherService.getVoucherByNameOrCode(searchTerm, pageable);
//        return new ApiResponse<>(HttpStatus.OK.value(), "Voucher", vouchers);
//    }


    @GetMapping("/findByTotalAmount")
    public ResponseEntity<List<VoucherResponse>> getValidVouchers(@RequestParam BigDecimal totalAmount) {
        List<VoucherResponse> vouchers = voucherService.getValidVouchers(totalAmount);
        return ResponseEntity.ok(vouchers);
    }

}
