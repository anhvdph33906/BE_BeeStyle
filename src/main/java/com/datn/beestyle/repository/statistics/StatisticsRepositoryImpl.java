package com.datn.beestyle.repository.statistics;

import com.datn.beestyle.dto.statistics.InventoryResponse;
import com.datn.beestyle.dto.statistics.RevenueStatisticsResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Repository
@Slf4j
public class StatisticsRepositoryImpl {
    @PersistenceContext
    private EntityManager entityManager;

    //    Thống kê doanh thu, sản phẩm theo ngày, tháng, năm
    public Page<RevenueStatisticsResponse> findRevenueByPeriod(String period, Pageable pageable, String periodValue) {
        // Validate period
        if (!List.of("day", "month", "year", "range").contains(period)) {
            throw new IllegalArgumentException("Invalid period. Must be 'day', 'month', 'year', or 'range'.");
        }

        // Xử lý periodValue mặc định nếu null hoặc rỗng
        periodValue = getDefaultPeriodValue(period, periodValue);

        // Xây dựng các thành phần truy vấn động
        QueryComponents components = buildQueryComponents(period, periodValue);

        // Thực thi truy vấn chính
        String sql = String.format("""
                            SELECT %s, 
                                  SUM(DISTINCT o.total_amount) AS revenue,
                                   SUM(oi.quantity) AS quantity
                              FROM `order` o
                              JOIN `order_item` oi ON o.id = oi.order_id
                              WHERE (o.order_status = 6 OR o.order_status = 1) AND %s
                              %s
                              ORDER BY %s;
                        """, components.selectClause, components.whereClause,
                period.equals("range") ? "" : "GROUP BY " + components.groupByClause, components.orderByClause);

        Query query = entityManager.createNativeQuery(sql, "RevenueByPeriodMapping");
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        @SuppressWarnings("unchecked")
        List<RevenueStatisticsResponse> results = query.getResultList();

        // Thực thi truy vấn đếm tổng phần tử
        String countSql = String.format("""
                    SELECT COUNT(*) 
                      FROM `order` o
                      JOIN `order_item` oi ON o.id = oi.order_id
                      WHERE o.order_status = 6 AND %s
                """, components.whereClause);

        Query countQuery = entityManager.createNativeQuery(countSql);
        long totalElements = ((Number) countQuery.getSingleResult()).longValue();

        // Trả về kết quả phân trang
        return new PageImpl<>(results, pageable, totalElements);
    }

    //    Thống kê hóa đơn theo ngày, tháng, năm
    public Page<RevenueStatisticsResponse> findOrderStatusByPeriod(String period, Pageable pageable, String periodValue) {
        // Validate period
        if (!List.of("day", "month", "year", "range").contains(period)) {
            throw new IllegalArgumentException("Invalid period. Must be 'day', 'month', 'year', or 'range'.");
        }

        // Xử lý periodValue mặc định nếu null hoặc rỗng
        periodValue = getDefaultPeriodValue(period, periodValue);

        // Xây dựng các thành phần truy vấn động
        QueryComponents components = buildQueryComponents(period, periodValue);

        // Cập nhật SELECT để đếm trạng thái đơn hàng
        String sql = String.format("""
                            SELECT %s, 
                                   COUNT(CASE WHEN o.order_status IN (1, 6) THEN 1 END) AS total_success,
                                   COUNT(CASE WHEN o.order_status = 7 THEN 1 END) AS total_failed
                              FROM `order` o
                              WHERE %s
                              %s
                              ORDER BY %s;
                        """, components.selectClause, components.whereClause,
                period.equals("range") ? "" : "GROUP BY " + components.groupByClause, components.orderByClause);

        Query query = entityManager.createNativeQuery(sql, "OrderStatusByPeriodMapping");
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        @SuppressWarnings("unchecked")
        List<RevenueStatisticsResponse> results = query.getResultList();

        // Truy vấn đếm tổng phần tử
        String countSql = String.format("""
                    SELECT COUNT(*) 
                      FROM `order` o
                      WHERE %s
                """, components.whereClause);

        Query countQuery = entityManager.createNativeQuery(countSql);
        long totalElements = ((Number) countQuery.getSingleResult()).longValue();

        // Trả về kết quả phân trang
        return new PageImpl<>(results, pageable, totalElements);
    }

    public List<InventoryResponse> filterProductVariantsByStock(int stock) {
        String sql = """
                    SELECT 
                        pv.id AS id, 
                        p.id AS productId, 
                        p.product_name AS productName, 
                        pv.sku AS sku, 
                        c.id AS colorId,
                        c.color_code AS colorCode,
                        c.color_name AS colorName, 
                        s.id AS sizeId,
                        s.size_name AS sizeName, 
                        pv.quantity_in_stock AS quantityInStock,
                        pi.image_url AS imageUrl
                    FROM 
                        product_variant pv
                    JOIN 
                        product p ON pv.product_id = p.id
                    LEFT JOIN 
                        color c ON pv.color_id = c.id
                    LEFT JOIN 
                        size s ON pv.size_id = s.id
                    LEFT JOIN 
                        product_image pi ON p.id = pi.product_id AND pi.is_default = 1
                    WHERE 
                        pv.quantity_in_stock <= ?
                """;

        Query query = entityManager.createNativeQuery(sql, "ProductVariantResponseMappingByStock");
        query.setParameter(1, stock);

        @SuppressWarnings("unchecked")
        List<InventoryResponse> results = query.getResultList();

        return results; // Trả về toàn bộ danh sách
    }

    public Page<InventoryResponse> TopSellingProduct(Pageable pageable, int top) {
        // Truy vấn chính để lấy dữ liệu với LIMIT và OFFSET
        String sql = """
                SELECT
                    pv.id AS id, 
                    p.id AS productId, 
                    p.product_name AS productName, 
                    pv.sku AS sku, 
                    c.id AS colorId,
                    c.color_code AS colorCode,
                    c.color_name AS colorName, 
                    s.id AS sizeId,
                    s.size_name AS sizeName, 
                    pi.image_url AS imageUrl,
                    SUM(oi.quantity) AS totalQuantitySold
                FROM
                    product_variant pv
                INNER JOIN
                    product p ON pv.product_id = p.id
                LEFT JOIN
                    color c ON pv.color_id = c.id
                LEFT JOIN
                    size s ON pv.size_id = s.id
                INNER JOIN
                    order_item oi ON pv.id = oi.product_variant_id
                INNER JOIN
                    `order` o ON oi.order_id = o.id
                LEFT JOIN
                    product_image pi ON p.id = pi.product_id AND pi.is_default = 1
                WHERE
                    o.order_status IN (1, 6)
                GROUP BY
                    pv.id, p.id, p.product_name, c.color_name, s.size_name, pv.sku, pi.image_url
                ORDER BY
                    totalQuantitySold DESC
                LIMIT :limit OFFSET :offset
                """;

        // Tạo câu truy vấn với tham số LIMIT và OFFSET
        Query query = entityManager.createNativeQuery(sql, "ProductVariantResponseMappingByQuantitySold");

        // Thiết lập tham số LIMIT và OFFSET cho câu truy vấn
        query.setParameter("limit", top);  // Lấy top N sản phẩm bán chạy nhất
        query.setParameter("offset", pageable.getOffset());  // Dịch chuyển tới vị trí của trang

        @SuppressWarnings("unchecked")
        List<InventoryResponse> results = query.getResultList();

        // Truy vấn để đếm tổng số phần tử (tổng số sản phẩm bán chạy nhất)
        String countSql = """
                SELECT COUNT(DISTINCT p.id)
                FROM
                    product_variant pv
                INNER JOIN
                    product p ON pv.product_id = p.id
                LEFT JOIN
                    color c ON pv.color_id = c.id
                LEFT JOIN
                    size s ON pv.size_id = s.id
                INNER JOIN
                    order_item oi ON pv.id = oi.product_variant_id
                INNER JOIN
                    `order` o ON oi.order_id = o.id
                WHERE
                    o.order_status IN (1, 6)
                """;

        Query countQuery = entityManager.createNativeQuery(countSql);
        long totalElements = ((Number) countQuery.getSingleResult()).longValue();

        // Trả về kết quả phân trang
        return new PageImpl<>(results, pageable, totalElements);
    }


    // Lấy giá trị mặc định cho periodValue
    private String getDefaultPeriodValue(String period, String periodValue) {
        if (periodValue == null || periodValue.isEmpty()) {
            switch (period) {
                case "day":
                    return LocalDate.now().toString(); // Ngày hiện tại
                case "month":
                    return String.valueOf(LocalDate.now().getYear()); // Năm hiện tại
                case "year":
                    return String.valueOf(LocalDate.now().getYear()); // Năm hiện tại
                case "range":
                    return LocalDate.now() + "," + LocalDate.now(); // Hôm nay
            }
        }
        return periodValue;
    }

    // Tạo các thành phần truy vấn động
    private QueryComponents buildQueryComponents(String period, String periodValue) {
        QueryComponents components = new QueryComponents();

        switch (period) {
            case "day":
                components.selectClause = "DATE(o.payment_date) AS period";
                components.groupByClause = "DATE(o.payment_date) ";
                components.whereClause = String.format(
                        "DATE(o.payment_date) BETWEEN DATE_SUB('%s', INTERVAL 6 DAY) AND '%s'",
                        periodValue, periodValue
                );
                components.orderByClause = "DATE(o.payment_date) ASC";
                break;

            case "month":
                components.selectClause = "DATE_FORMAT(o.payment_date, '%Y-%m') AS period";
                components.groupByClause = "DATE_FORMAT(o.payment_date, '%Y-%m')";
                components.whereClause = "YEAR(o.payment_date) = '" + periodValue + "'";
                components.orderByClause = "DATE_FORMAT(o.payment_date, '%Y-%m') ASC";
                break;

            case "year":
                components.selectClause = "YEAR(o.payment_date) AS period";
                components.groupByClause = "YEAR(o.payment_date)";
                components.whereClause = String.format(
                        "YEAR(o.payment_date) BETWEEN ('%s' - 4) AND '%s'",
                        periodValue, periodValue);
                components.orderByClause = "YEAR(o.payment_date) ASC";
                break;

            case "range":
                String[] dateRange = periodValue.split(",");
                if (dateRange.length != 2) {
                    throw new IllegalArgumentException("Invalid range format. Expected 'startDate,endDate'.");
                }
                String startDate = dateRange[0];
                String endDate = dateRange[1];
                components.selectClause = String.format("'%s - %s' AS period", startDate, endDate);
                components.whereClause = String.format(
                        "DATE(o.payment_date) BETWEEN '%s' AND '%s'",
                        startDate, endDate
                );
                components.orderByClause = "DATE(o.payment_date) ASC";
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + period);
        }
        return components;
    }

    // Lớp hỗ trợ cấu hình các thành phần truy vấn
    private static class QueryComponents {
        String selectClause = "";
        String groupByClause = "";
        String whereClause = "";
        String orderByClause = "";
    }


}
