package com.datn.beestyle.repository;

import com.datn.beestyle.common.IGenericRepository;
import com.datn.beestyle.dto.voucher.VoucherResponse;
import com.datn.beestyle.entity.Voucher;
import com.datn.beestyle.entity.product.attributes.Material;
import com.datn.beestyle.entity.product.attributes.Size;
import com.datn.beestyle.enums.DiscountType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface VoucherRepository extends IGenericRepository<Voucher, Integer> {

    //    @Query("""
//                select v from Voucher v
//                where
//                    (:name is null or v.voucherName like concat('%', :name, '%')) and
//                    (:status is null or v.status = :status) and
//                    (:discountType is null or v.discountType = :discountType) and
//                    (:startDate IS NULL OR v.startDate >= :startDate) AND
//                    (:endDate IS NULL OR v.endDate <= :endDate)
//            """)
//    Page<Voucher> findByNameContainingAndStatus(Pageable pageable,
//                                                @Param("name") String name,
//                                                @Param("status") Integer status,
//                                                @Param("discountType") Integer discountType,
//                                                @Param("startDate") Timestamp startDate,
//                                                @Param("endDate") Timestamp endDate);
    @Query("""
                select new com.datn.beestyle.dto.voucher.VoucherResponse(
                    v.id, v.voucherName, v.voucherCode, 
                    v.discountType, v.discountValue, v.maxDiscount, 
                    v.minOrderValue, v.startDate, v.endDate, 
                    v.usageLimit, v.usagePerUser, v.note,
                    v.status, v.createdAt, v.updatedAt) 
                from Voucher v 
                where 
                    (:name is null or v.voucherName like concat('%', :name, '%')) and
                    (:status is null or v.status = :status) and
                    (:discountType is null or v.discountType = :discountType) and
                    (:startDate IS NULL OR v.startDate >= :startDate) AND 
                    (:endDate IS NULL OR v.endDate <= :endDate)
            """)
    Page<VoucherResponse> findByNameContainingAndStatus(
            Pageable pageable,
            @Param("name") String name,
            @Param("status") Integer status,
            @Param("discountType") Integer discountType,
            @Param("startDate") Timestamp startDate,
            @Param("endDate") Timestamp endDate
    );

//    @Query("SELECT v FROM Voucher v WHERE v.status = :status AND v.minOrderValue <= :totalAmount")
//    List<Voucher> findValidVouchers(@Param("status") int status, @Param("totalAmount") BigDecimal totalAmount);
    @Query("""
                select new com.datn.beestyle.dto.voucher.VoucherResponse(
                        v.id, v.voucherName, v.voucherCode, 
                        v.discountType, v.discountValue, v.maxDiscount, 
                        v.minOrderValue, v.startDate, v.endDate, 
                        v.usageLimit, v.usagePerUser, v.note,
                        v.status, v.createdAt, v.updatedAt) 
                from Voucher v 
            WHERE v.status = :status AND v.minOrderValue <= :totalAmount
            """)
    List<VoucherResponse> findValidVouchers(@Param("status") int status, @Param("totalAmount") BigDecimal totalAmount);

    boolean existsByVoucherName(String voucherName);
    boolean existsByVoucherCode(String voucherCode);
}
