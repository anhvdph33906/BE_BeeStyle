package com.datn.beestyle.repository;

import com.datn.beestyle.common.IGenericRepository;
import com.datn.beestyle.dto.material.MaterialResponse;
import com.datn.beestyle.entity.product.attributes.Material;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialRepository extends IGenericRepository<Material, Integer> {
    @Query("""
            select m from Material m 
            where 
                (:name is null or m.materialName like concat('%', :name, '%')) and 
                (:status is null or m.status = :status)
            """)
    Page<Material> findByNameContainingAndStatus(Pageable pageable,
                                                 @Param("name") String name,
                                                 @Param("status") Integer status);

    @Query("""
            select new com.datn.beestyle.dto.material.MaterialResponse(m.id, m.materialName) 
            from Material m 
            where m.status = 1
            order by m.createdAt desc , m.id desc 
            """)
    List<MaterialResponse> findAllByStatusIsActive();

    boolean existsByMaterialName(String name);
}
