package com.datn.beestyle.repository.customer;

import com.datn.beestyle.dto.customer.CustomerResponse;
import com.datn.beestyle.dto.statistics.RevenueStatisticsResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
public class CustomerRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    public Page<CustomerResponse> findProductSalesByUser(Pageable pageable,Long id){

        String sql = String.format("""
                SELECT p.product_name AS product_name, pv.sale_price AS sale_price, SUM(oi.quantity) AS total_quantity,
                pi.image_url AS image_product
                FROM `customer` c
                JOIN `order` o ON o.customer_id = c.id
                JOIN `order_item` oi ON oi.order_id = o.id
                JOIN `product_variant` pv ON pv.id = oi.product_variant_id
                JOIN `product` p ON p.id = pv.product_id
                LEFT JOIN `product_image` pi ON pi.product_id = p.id AND pi.is_default = 1
                WHERE c.id = %s and o.order_status IN (1, 6)
                GROUP BY product_name, pv.sale_price, pi.image_url;
                """,id);

        Query query = entityManager.createNativeQuery(sql, "ProductSalesByUserMapping");
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        @SuppressWarnings("unchecked")
        List<CustomerResponse> results = query.getResultList();

        String countSql = String.format("""
                    SELECT COUNT(*)
                    FROM (
                     SELECT
                      p.product_name,pv.sale_price,SUM(oi.quantity) AS total_quantity,
                      pi.image_url AS default_image
                      FROM `customer` c
                      JOIN `order` o ON o.customer_id = c.id
                       JOIN `order_item` oi ON oi.order_id = o.id
                       JOIN `product_variant` pv ON pv.id = oi.product_variant_id
                       JOIN `product` p ON p.id = pv.product_id
                       LEFT JOIN `product_image` pi ON pi.product_id = p.id AND pi.is_default = 1
                        WHERE c.id = %s AND o.order_status IN (1, 6)
                        GROUP BY p.product_name, pv.sale_price, pi.image_url
                        ) AS subquery;
                """,id);

        Query countQuery = entityManager.createNativeQuery(countSql);
        long totalElements = ((Number) countQuery.getSingleResult()).longValue();

        // Trả về kết quả phân trang
        return new PageImpl<>(results, pageable, totalElements);
    }
}
