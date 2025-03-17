package com.datn.beestyle.repository.product;

import com.datn.beestyle.dto.product.ProductResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
@Slf4j
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<ProductResponse> filterProduct(Pageable pageable, String keyword, List<Integer> categoryIds, Integer genderProduct,
                                               List<Integer> brandIds, List<Integer> materialIds,
                                               BigDecimal minPrice, BigDecimal maxPrice, Integer status) {
        int page = 0, pageSize = 20;
        if (pageable.getPageNumber() > 0) page = pageable.getPageNumber() - 1;
        if (pageable.getPageSize() > 0) pageSize = pageable.getPageSize();
        PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.by("createdAt", "id").descending());

        StringBuilder sqlQuery = new StringBuilder("""
                    WITH MinPriceOfProduct AS (
                        SELECT p.id, MIN(pv.sale_price) AS min_sale_price, SUM(pv.quantity_in_stock) AS total_product_in_stock
                        FROM product p
                            JOIN product_variant pv ON p.id = pv.product_id
                        GROUP BY p.id
                    )
                    SELECT p.id, p.product_code, p.product_name, pi.image_url, mp.min_sale_price, mp.total_product_in_stock, p.created_at
                    FROM product p
                    LEFT JOIN product_image pi ON p.id = pi.product_id AND pi.is_default = true
                    LEFT JOIN MinPriceOfProduct mp ON p.id = mp.id
                    WHERE 1=1                   
                """);

        handleStringConditionQuery(sqlQuery, keyword, categoryIds, genderProduct, brandIds, materialIds, minPrice, maxPrice, status);
        sqlQuery.append(" ORDER BY p.created_at DESC, p.id DESC;");

        Query query = entityManager.createNativeQuery(String.valueOf(sqlQuery), "ProductResponseMapping");
        handleParamConditionQuery(query, keyword, categoryIds, genderProduct, brandIds, materialIds, minPrice, maxPrice, status);

        int size = pageRequest.getPageSize();
        int offset = page * size;
        query.setFirstResult(offset);
        query.setMaxResults(size);
        List<ProductResponse> productResponse = query.getResultList();

        // count users
        StringBuilder sqlCountQuery = new StringBuilder("""
                WITH MinPriceOfProduct AS (
                    SELECT p.id, MIN(pv.sale_price) AS min_sale_price, SUM(pv.quantity_in_stock) AS total_product_in_stock
                    FROM product p
                        JOIN product_variant pv ON p.id = pv.product_id
                    GROUP BY p.id
                )
                SELECT COUNT(*)
                FROM product p
                LEFT JOIN MinPriceOfProduct mp ON p.id = mp.id
                WHERE 1=1
            """);
        handleStringConditionQuery(sqlCountQuery, keyword, categoryIds, genderProduct, brandIds, materialIds, minPrice, maxPrice, status);
        Query countQuery = entityManager.createNativeQuery(String.valueOf(sqlCountQuery));
        handleParamConditionQuery(countQuery, keyword, categoryIds, genderProduct, brandIds, materialIds, minPrice, maxPrice, status);
        Long totalElements = (Long) countQuery.getSingleResult();

        return new PageImpl<>(productResponse, pageRequest, totalElements);
    }

    private void handleStringConditionQuery(StringBuilder query, String keyword, List<Integer> categoryIds, Integer genderProduct,
                                            List<Integer> brandIds, List<Integer> materialIds,
                                            BigDecimal minPrice, BigDecimal maxPrice, Integer status) {
        if (keyword != null && !keyword.isBlank()) {
            query.append(" AND (p.product_code LIKE CONCAT('%', :keyword, '%') OR p.product_name LIKE CONCAT('%', :keyword, '%'))");
        }
        if (categoryIds != null && !categoryIds.isEmpty()) query.append(" AND p.category_id IN :categoryIds");
        if (genderProduct != null) query.append(" AND p.gender = :genderProduct");
        if (brandIds != null && !brandIds.isEmpty()) query.append(" AND p.brand_id IN (:brandIds)");
        if (materialIds != null && !materialIds.isEmpty()) query.append(" AND p.material_id IN (:materialIds)");
        if (minPrice != null) query.append(" AND mp.min_sale_price >= :minPrice");
        if (maxPrice != null) query.append(" AND mp.min_sale_price <= :maxPrice");
        if (status != null) query.append(" AND p.status = :status");
    }

    private void handleParamConditionQuery(Query query, String keyword, List<Integer> categoryIds, Integer genderProduct,
                                            List<Integer> brandIds, List<Integer> materialIds,
                                            BigDecimal minPrice, BigDecimal maxPrice, Integer status) {
        if (keyword != null && !keyword.isBlank()) query.setParameter("keyword", keyword);
        if (categoryIds != null && !categoryIds.isEmpty()) query.setParameter("categoryIds", categoryIds);
        if (genderProduct != null) query.setParameter("genderProduct", genderProduct);
        if (brandIds != null && !brandIds.isEmpty()) query.setParameter("brandIds", brandIds);
        if (materialIds != null && !materialIds.isEmpty()) query.setParameter("materialIds", materialIds);
        if (minPrice != null) query.setParameter("minPrice", minPrice);
        if (maxPrice != null) query.setParameter("maxPrice", maxPrice);
        if (status != null) query.setParameter("status", status);
    }
}
