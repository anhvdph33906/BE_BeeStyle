package com.datn.beestyle.controller;

import com.datn.beestyle.dto.ApiResponse;
import com.datn.beestyle.dto.promotion.CreatePromotionRequest;
import com.datn.beestyle.dto.promotion.UpdatePromotionRequest;
import com.datn.beestyle.service.promotion.IPromotionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Validated
@RestController
@RequestMapping("/admin/promotion")
@RequiredArgsConstructor
@Tag(name = "Promotion Controller")
public class PromotionController {

    private final IPromotionService promotionService;

    @Operation(method = "GET", summary = "Get list of promotions",
            description = "Send a request via this API to get promotion list and search with paging by name and status")
    @GetMapping
    public ApiResponse<?> getPromotions(Pageable pageable,
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
        return new ApiResponse<>(HttpStatus.OK.value(), "Promotions",
                promotionService.getAllByNameAndStatus(pageable, name, status,discountType, startTimestamp, endTimestamp));
    }

    @PostMapping("/create")
    public ApiResponse<?> createPromotion(@Valid @RequestBody CreatePromotionRequest request) {
        return new ApiResponse<>(HttpStatus.CREATED.value(), "Thêm đợt khuyến mại thành công!",
                promotionService.create(request));
    }

    @PostMapping("/creates")
    public ApiResponse<?> createPromotions(@RequestBody List<@Valid CreatePromotionRequest> requestList) {
        return new ApiResponse<>(HttpStatus.CREATED.value(), "Promotions added successfully",
                promotionService.createEntities(requestList));
    }

    @PutMapping("/update/{id}")
    public ApiResponse<?> updatePromotion(@Min(1) @PathVariable int id,
                                          @Valid @RequestBody UpdatePromotionRequest request) {
        return new ApiResponse<>(HttpStatus.CREATED.value(), "Sửa đợt khuyến mại thành công!",
                promotionService.update(id, request));
    }

    @PatchMapping("/updates")
    public ApiResponse<?> updatePromotions(@RequestBody List<@Valid UpdatePromotionRequest> requestList) {
        promotionService.updateEntities(requestList);
        return new ApiResponse<>(HttpStatus.CREATED.value(), "Promotions updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<?> deletePromotion(@Min(1) @PathVariable int id) {
        promotionService.deletePromotion(id);
        return new ApiResponse<>(HttpStatus.OK.value(), "Xóa đợt khuyến mại thành công!");
    }

    @GetMapping("/{id}")
    public ApiResponse<?> getPromotion(@Min(1) @PathVariable int id) {
        return new ApiResponse<>(HttpStatus.OK.value(), "Promotion", promotionService.getDtoById(id));
    }

}
