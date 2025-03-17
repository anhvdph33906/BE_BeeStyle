package com.datn.beestyle.service.statistics;

import com.datn.beestyle.dto.PageResponse;
import com.datn.beestyle.dto.statistics.InventoryResponse;
import com.datn.beestyle.dto.statistics.RevenueStatisticsResponse;
import com.datn.beestyle.repository.statistics.StatisticsRepositoryImpl;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class RevenueStatisticsService {

    private final StatisticsRepositoryImpl statisticsRepository;

    public RevenueStatisticsService(StatisticsRepositoryImpl statisticsRepository) {
        this.statisticsRepository = statisticsRepository;
    }


    public PageResponse<List<RevenueStatisticsResponse>> getRevenueByPeriod(String period, Pageable pageable, String periodValue ) {
        // Nếu period là null, gán giá trị mặc định là "day"
        if (period == null) {
            period = "day";
        }

        // Nếu periodValue là null hoặc rỗng, gán giá trị mặc định là ngày hôm nay
        if (periodValue == null || periodValue.isEmpty()) {
            switch (period) {
                case "range":
                    // Nếu period là "range" và periodValue rỗng, tự động lấy ngày hôm nay làm ngày bắt đầu và kết thúc
                    periodValue = LocalDate.now().toString() + "," + LocalDate.now().toString();
                    break;
                default:
                    periodValue = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); // Định dạng ngày theo "yyyy-MM-dd"
            }
        }


        Page<RevenueStatisticsResponse> revenueStatisticsDTOPages = statisticsRepository.findRevenueByPeriod(period,pageable,periodValue);

        // Tạo và trả về đối tượng PageResponse với dữ liệu đã lấy
        return PageResponse.<List<RevenueStatisticsResponse>>builder()
                .pageNo(revenueStatisticsDTOPages.getNumber() + 1)
                .pageSize(revenueStatisticsDTOPages.getSize())
                .totalElements(revenueStatisticsDTOPages.getTotalElements())
                .totalPages(revenueStatisticsDTOPages.getTotalPages())
                .items(revenueStatisticsDTOPages.getContent())
                .build();
    }

    public PageResponse<List<RevenueStatisticsResponse>> getOrderStatusByPeriod(String period, Pageable pageable, String periodValue ) {

        // Nếu period là null, gán giá trị mặc định là "day"
        if (period == null) {
            period = "day";
        }

        // Nếu periodValue là null hoặc rỗng, gán giá trị mặc định là ngày hôm nay
        if (periodValue == null || periodValue.isEmpty()) {
            switch (period) {
                case "range":
                    // Nếu period là "range" và periodValue rỗng, tự động lấy ngày hôm nay làm ngày bắt đầu và kết thúc
                    periodValue = LocalDate.now().toString() + "," + LocalDate.now().toString();
                    break;
                default:
                    periodValue = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); // Định dạng ngày theo "yyyy-MM-dd"
            }
        }


        Page<RevenueStatisticsResponse> revenueStatisticsDTOPages = statisticsRepository.findOrderStatusByPeriod(period,pageable,periodValue);

        // Tạo và trả về đối tượng PageResponse với dữ liệu đã lấy
        return PageResponse.<List<RevenueStatisticsResponse>>builder()
                .pageNo(revenueStatisticsDTOPages.getNumber() + 1)
                .pageSize(revenueStatisticsDTOPages.getSize())
                .totalElements(revenueStatisticsDTOPages.getTotalElements())
                .totalPages(revenueStatisticsDTOPages.getTotalPages())
                .items(revenueStatisticsDTOPages.getContent())
                .build();
    }



//    public PageResponse<List<RevenueStatisticsResponse>> getRevenueByMonth(LocalDate startDate, LocalDate endDate, Pageable pageable) {
//        Date sqlStartDate = Date.valueOf(startDate);
//        Date sqlEndDate = Date.valueOf(endDate);
//
//        List<RevenueStatisticsResponse> statistics = (List<RevenueStatisticsResponse>) statisticsRepository.findRevenueByMonth(sqlStartDate,sqlEndDate,pageable);
//
//        return createPageResponse(statistics, pageable);
//    }


//
//    public PageResponse<List<RevenueStatisticsResponse>> getRevenueByYear(LocalDate startDate, LocalDate endDate, Pageable pageable) {
//        List<Object[]> results = orderRepository.findRevenueByYear(startDate, endDate);
//
//        List<RevenueStatisticsResponse> statistics = results.stream()
//                .map(result -> new RevenueStatisticsResponse(result[0].toString(), (BigDecimal) result[1]))
//                .collect(Collectors.toList());
//
//        return createPageResponse(statistics, pageable);
//    }

//    public PageResponse<List<RevenueStatisticsResponse>> getTopSellingProductsByFilterType(String filterType,Pageable pageable) {
//        List<RevenueStatisticsResponse> statistics = orderRepository.findTopSellingProductsByFilterType(pageable,filterType);
//        return createPageResponse(statistics, pageable);
//    }



    private PageResponse<List<RevenueStatisticsResponse>> createPageResponse(List<RevenueStatisticsResponse> statistics, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        List<RevenueStatisticsResponse> paginatedList;

        if (statistics.size() < startItem) {
            paginatedList = new ArrayList<>();
        } else {
            int toIndex = Math.min(startItem + pageSize, statistics.size());
            paginatedList = statistics.subList(startItem, toIndex);
        }

        Page<RevenueStatisticsResponse> page = new PageImpl<>(paginatedList, pageable, statistics.size());

        return PageResponse.<List<RevenueStatisticsResponse>>builder()
                .pageNo(page.getNumber() + 1)
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .items(page.getContent())
                .build();
    }
    //Thống kê sản phẩm tồn
    public List<InventoryResponse> getProductVariantsByStock(int stock) {
        List<InventoryResponse> responses = statisticsRepository.filterProductVariantsByStock(stock);
        responses.sort(Comparator.comparingInt(InventoryResponse::getQuantityInStock));
        return responses;
    }


    public PageResponse<List<InventoryResponse>> getTopSellingProduct(Pageable pageable, int top) {
        // Lấy danh sách sản phẩm theo số lượng tồn kho
        Page<InventoryResponse> productVariantResponsePages = statisticsRepository.TopSellingProduct(pageable, top);

        // Trả về kết quả phân trang
        return PageResponse.<List<InventoryResponse>>builder()
                .pageNo(productVariantResponsePages.getNumber() + 1)
                .pageSize(productVariantResponsePages.getSize())
                .totalElements(productVariantResponsePages.getTotalElements())
                .totalPages(productVariantResponsePages.getTotalPages())
                .items(productVariantResponsePages.getContent())
                .build();
    }

}
