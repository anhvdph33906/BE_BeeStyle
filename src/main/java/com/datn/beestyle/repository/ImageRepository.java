package com.datn.beestyle.repository;

import com.datn.beestyle.common.IGenericRepository;
import com.datn.beestyle.dto.product.attributes.image.ImageReponse;
import com.datn.beestyle.entity.product.ProductImage;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends IGenericRepository<ProductImage, Long> {
    @Query(
            """
                     select distinct 
                            new com.datn.beestyle.dto.product.attributes.image.ImageReponse(
                                pi.id,
                                pi.imageUrl,
                                pi.isDefault
                            )
                            from ProductImage pi
                            where pi.product.id = :productId            
             """
    )
    List<ImageReponse> getImageByProductIds(@Param("productId") Long productId);
}
