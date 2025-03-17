package com.datn.beestyle.repository.product;

import com.datn.beestyle.dto.product.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.math.BigDecimal;
import java.util.List;

public interface ProductRepositoryCustom {

    Page<ProductResponse> filterProduct(Pageable pageable, String keyword, List<Integer> categoryIds, Integer genderProduct,
                                        List<Integer> brandIds, List<Integer> materialIds, BigDecimal minPrice,
                                        BigDecimal maxPrice, Integer status);
}
