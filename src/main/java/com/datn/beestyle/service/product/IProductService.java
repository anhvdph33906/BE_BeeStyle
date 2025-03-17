package com.datn.beestyle.service.product;

import com.datn.beestyle.common.IGenericService;
import com.datn.beestyle.dto.PageResponse;
import com.datn.beestyle.dto.product.CreateProductRequest;
import com.datn.beestyle.dto.product.ProductResponse;
import com.datn.beestyle.dto.product.UpdateProductRequest;
import com.datn.beestyle.entity.product.Product;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface IProductService
        extends IGenericService<Product, Long, CreateProductRequest, UpdateProductRequest, ProductResponse> {

    PageResponse<List<ProductResponse>> searchProductByStatusIsActive(Pageable pageable, String keyword);

    PageResponse<List<ProductResponse>> filterProductByStatusIsActive(Pageable pageable, String keyword, String categoryIds, String genderProduct,
                                                                      String brandIds, String materialIds,
                                                                      BigDecimal minPrice, BigDecimal maxPrice);

    PageResponse<List<ProductResponse>> getProductsFilterByFields(Pageable pageable, String keyword,
                                                                  String category, String gender, String brandIds,
                                                                  String materialIds, String status);

}
