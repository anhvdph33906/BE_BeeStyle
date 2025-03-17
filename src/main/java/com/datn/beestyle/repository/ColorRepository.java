package com.datn.beestyle.repository;

import com.datn.beestyle.common.IGenericRepository;
import com.datn.beestyle.dto.product.attributes.color.ColorResponse;
import com.datn.beestyle.entity.product.attributes.Color;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColorRepository extends IGenericRepository<Color, Integer> {

    @Query("""
            select c from Color c 
            where 
                (:name is null or c.colorName like concat('%', :name, '%')) and
                (:status is null or c.status = :status)
            """)
    Page<Color> findByNameContainingAndStatus(Pageable pageable,
                                               @Param("name") String name,
                                               @Param("status") Integer status);

    @Query("""
            select new com.datn.beestyle.dto.product.attributes.color.ColorResponse(c.id, c.colorCode, c.colorName) 
            from Color c 
            where c.status = 1
            order by c.createdAt desc , c.id desc 
            """)
    List<ColorResponse> findAllByStatusIsActive();

    @Query("""
            select distinct 
            new com.datn.beestyle.dto.product.attributes.color.ColorResponse(
                c.id, c.colorCode, c.colorName
            ) 
            from Color c 
            inner join ProductVariant pv on pv.color.id = c.id 
            where pv.product.id = :productId and c.status = 1
            order by c.id 
            """)
    List<ColorResponse> findAllByProductVariant(@Param("productId") Long productId);

    boolean existsByColorName(String name);
}
