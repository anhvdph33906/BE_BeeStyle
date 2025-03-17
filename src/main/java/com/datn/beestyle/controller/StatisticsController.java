package com.datn.beestyle.controller;

import com.datn.beestyle.dto.ApiResponse;
import com.datn.beestyle.service.statistics.RevenueStatisticsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;


@RestController
@RequestMapping("/admin/statistics")
@RequiredArgsConstructor
@Tag(name = "Statistics Controller")
public class StatisticsController {
    @Autowired
    private RevenueStatisticsService revenueStatisticsService;

    @GetMapping("/revenue-by-period")
    public ApiResponse<?> getRevenueByPeriod(
            Pageable pageable,
            @RequestParam(required = false) String period,
            @RequestParam(required = false) String periodValue) {

        return new ApiResponse<>(HttpStatus.OK.value(), "Thống kê doanh thu, sản phẩm theo ngày, tháng, năm",
                revenueStatisticsService.getRevenueByPeriod(period, pageable,periodValue));

    }
    @GetMapping("/orderStatus-by-period")
    public ApiResponse<?> getOrderStatusByPeriod(
            Pageable pageable,
            @RequestParam(required = false) String period,
            @RequestParam(required = false) String periodValue) {

        return new ApiResponse<>(HttpStatus.OK.value(), "Thống kê đơn hàng theo ngày, tháng, năm",
                revenueStatisticsService.getOrderStatusByPeriod(period, pageable,periodValue));

    }

    //    @GetMapping("/revenue-by-month")
//    public ApiResponse<?> getRevenueByMonth(
//            Pageable pageable,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
//
//        return new ApiResponse<>(HttpStatus.OK.value(), "Thống kê",
//                revenueStatisticsService.getRevenueByMonth(startDate, endDate,pageable));
//
//    }
//    @GetMapping("/top-selling-products")
//    public ApiResponse<?> opSellingProducts(
//            Pageable pageable,
//            @RequestParam String filterType) {
//
//        return new ApiResponse<>(HttpStatus.OK.value(), "Thống kê",
//                revenueStatisticsService.getTopSellingProductsByFilterType(filterType,pageable));
//
//    }
    @GetMapping("/filterByStock")
    public ApiResponse<?> getProductVariants(@RequestParam("stock") int stock) {
        return new ApiResponse<>(HttpStatus.OK.value(), "",
                revenueStatisticsService.getProductVariantsByStock(stock));
    }
    @GetMapping("/topSellingProduct")
    public ApiResponse<?> gettopSellingProduct(Pageable pageable, @RequestParam("top") int top) {
        return new ApiResponse<>(HttpStatus.OK.value(), "",
                revenueStatisticsService.getTopSellingProduct(pageable, top));
    }
}
