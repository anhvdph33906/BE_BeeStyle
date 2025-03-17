package com.datn.beestyle.repository;

import com.datn.beestyle.common.IGenericRepository;
import com.datn.beestyle.dto.promotion.PromotionResponse;
import com.datn.beestyle.entity.Promotion;
import com.datn.beestyle.entity.Voucher;
import com.datn.beestyle.entity.product.attributes.Brand;
import com.datn.beestyle.enums.DiscountType;
import jakarta.websocket.server.PathParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface PromotionRepository extends IGenericRepository<Promotion, Integer> {

//    @Query("""
//                select p from Promotion p
//                where
//                    (:name is null or p.promotionName like concat('%', :name, '%')) and
//                    (:status is null or p.status = :status) and
//                    (:discountType is null or p.discountType = :discountType)and
//                    (:startDate IS NULL OR p.startDate >= :startDate) AND
//                    (:endDate IS NULL OR p.endDate <= :endDate)
//            """)
//    Page<Promotion> findByNameContainingAndStatus(Pageable pageable,
//                                                  @Param("name") String name,
//                                                  @Param("status") Integer status,
//                                                  @Param("discountType") Integer discountType,
//                                                  @Param("startDate") Timestamp startDate,
//                                                  @Param("endDate") Timestamp endDate);
@Query("""
        select new com.datn.beestyle.dto.promotion.PromotionResponse(
            p.id, 
            p.promotionName, 
            p.discountType, 
            p.discountValue, 
            p.startDate, 
            p.endDate, 
            p.createdBy, 
            p.updatedBy, 
            p.description, 
            p.status, 
            p.createdAt, 
            p.updatedAt
        )
        from Promotion p
        where 
            (:name is null or p.promotionName like concat('%', :name, '%')) and
            (:status is null or p.status = :status) and
            (:discountType is null or p.discountType = :discountType) and
            (:startDate is null or p.startDate >= :startDate) and
            (:endDate is null or p.endDate <= :endDate)
        """)
Page<PromotionResponse> findByNameContainingAndStatus(Pageable pageable,
                                                      @Param("name") String name,
                                                      @Param("status") Integer status,
                                                      @Param("discountType") Integer discountType,
                                                      @Param("startDate") Timestamp startDate,
                                                      @Param("endDate") Timestamp endDate);


    @Query("""
        select new com.datn.beestyle.dto.promotion.PromotionResponse(
            p.id, p.promotionName, p.discountType, p.discountValue, 
            p.startDate, p.endDate, p.createdBy, p.updatedBy, 
            p.description, p.status, p.createdAt, p.updatedAt
        )
        from Promotion p 
        where p.endDate < :currentDate
        """)
    List<PromotionResponse> findEndedPromotions(@Param("currentDate") Timestamp currentDate);



}
