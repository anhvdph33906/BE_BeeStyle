package com.datn.beestyle.service.product.variant;

import com.datn.beestyle.common.IGenericService;
import com.datn.beestyle.dto.PageResponse;
import com.datn.beestyle.dto.product.variant.CreateProductVariantRequest;
import com.datn.beestyle.dto.product.variant.PatchUpdateQuantityProductVariant;
import com.datn.beestyle.dto.product.variant.ProductVariantResponse;
import com.datn.beestyle.dto.product.variant.UpdateProductVariantRequest;
import com.datn.beestyle.entity.product.ProductVariant;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface IProductVariantService
        extends IGenericService<ProductVariant, Long, CreateProductVariantRequest, UpdateProductVariantRequest, ProductVariantResponse> {

    PageResponse<List<ProductVariantResponse>> getProductVariantsByFieldsByProductId(Pageable pageable, String productIdStr,
                                                                                     String keyword, String colorIds,
                                                                                     String sizeIds, String status);

    PageResponse<List<ProductVariantResponse>> filterProductVariantsByStatusIsActive(Pageable pageable, String productId,
                                                                                     String colorIds, String sizeIds,
                                                                                     BigDecimal minPrice, BigDecimal maxPrice);

    int updateQuantityProductVariant(PatchUpdateQuantityProductVariant request, String action);

    //    Optional<List<ProductVariantResponse>> getAllProductsWithDetails(List<Long> productIds);
    Optional<Object[]> getAllProductsWithDetails(List<Long> productIds);

    void updateProductVariantCreate(Integer promotionId, List<Integer> ids);

//    void updateProductVariantUpdate(Integer promotionId, List<Integer> ids);

}
