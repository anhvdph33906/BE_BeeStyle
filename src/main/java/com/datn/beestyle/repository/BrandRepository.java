package com.datn.beestyle.repository;

import com.datn.beestyle.common.IGenericRepository;
import com.datn.beestyle.dto.brand.BrandResponse;
import com.datn.beestyle.dto.material.MaterialResponse;
import com.datn.beestyle.entity.product.attributes.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BrandRepository extends IGenericRepository<Brand, Integer> {

    @Query("""
            select b from Brand b 
            where 
                (:name is null or b.brandName like concat('%', :name, '%')) and
                (:status is null or b.status = :status)
            """)
    Page<Brand> findByNameContainingAndStatus(Pageable pageable,
                                              @Param("name") String name,
                                              @Param("status") Integer status);

    @Query("""
            select new com.datn.beestyle.dto.brand.BrandResponse(b.id, b.brandName) 
            from Brand b 
            where b.status = 1
            order by b.createdAt desc , b.id desc 
            """)
    List<BrandResponse> findAllByStatusIsActive();

    boolean existsByBrandName(String name);
}
