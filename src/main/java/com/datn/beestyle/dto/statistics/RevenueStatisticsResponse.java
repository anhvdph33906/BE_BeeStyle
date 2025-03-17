package com.datn.beestyle.dto.statistics;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RevenueStatisticsResponse {
    private Date date;  // Ngày hoặc tháng hoặc năm
    private BigDecimal revenue;  // Doanh thu
    private Long quantity;
    private Long totalOderSuccess;
    private Long totalOderFailed;
    private Integer month;
    private Integer year;
    private String productName;
    private BigDecimal salePrice;
    private String period; // ngày/ tháng / năm


    public RevenueStatisticsResponse(Date date, BigDecimal revenue, Long quantity) {
        this.date = date;
        this.revenue = revenue;
        this.quantity = quantity;
    }
    public RevenueStatisticsResponse(String period, Long totalOderSuccess, Long totalOderFailed) {
        this.period = period;
        this.totalOderSuccess = totalOderSuccess;
        this.totalOderFailed = totalOderFailed;
    }
    public RevenueStatisticsResponse(String period, BigDecimal revenue, Long quantity) {
        this.period = period;
        this.revenue = revenue;
        this.quantity = quantity;

    }

    public RevenueStatisticsResponse(Integer month, Integer year, BigDecimal revenue, Long quantity) {
        this.month = month;
        this.year = year;
        this.revenue = revenue;
        this.quantity = quantity;
    }




}

