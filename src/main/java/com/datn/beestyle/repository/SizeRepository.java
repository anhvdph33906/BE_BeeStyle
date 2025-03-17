package com.datn.beestyle.repository;

import com.datn.beestyle.common.IGenericRepository;
import com.datn.beestyle.dto.product.attributes.color.ColorResponse;
import com.datn.beestyle.dto.product.attributes.size.SizeResponse;
import com.datn.beestyle.entity.product.attributes.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SizeRepository extends IGenericRepository<Size, Integer> {

    @Query("""
            select s from Size s 
            where 
                (:name is null or s.sizeName like concat('%', :name, '%')) and
                (:status is null or s.status = :status)
            """)
    Page<Size> findByNameContainingAndStatus(Pageable pageable,
                                             @Param("name") String name,
                                             @Param("status") Integer status);

    @Query("""
            select new com.datn.beestyle.dto.product.attributes.size.SizeResponse(s.id, s.sizeName) 
            from Size s
            where s.status = 1
            order by s.createdAt desc , s.id desc 
            """)
    List<SizeResponse> findAllByStatusIsActive();

    @Query("""
            select distinct 
            new com.datn.beestyle.dto.product.attributes.size.SizeResponse(s.id, s.sizeName) 
            from Size s
            inner join ProductVariant pv on pv.size.id = s.id 
            inner join Color c on c.id = pv.color.id 
            where pv.product.id = :productId and 
                  c.colorCode like :colorCode and 
                  s.status = 1 
            order by s.id 
            """)
    List<SizeResponse> findAllByProductVariant(
            @Param("productId") Long productId,
            @Param("colorCode") String colorCode
    );

    boolean existsBySizeName(String name);
}
