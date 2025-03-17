package com.datn.beestyle.service.user;

import com.datn.beestyle.dto.product.ProductResponse;
import com.datn.beestyle.dto.product.attributes.image.ImageReponse;
import com.datn.beestyle.repository.ImageRepository;
import com.datn.beestyle.repository.product.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Slf4j
@Service
public class UserProductHomeService {
    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;

    public UserProductHomeService(
            ProductRepository productRepository,
            ImageRepository imageRepository
    ) {
        this.productRepository = productRepository;
        this.imageRepository = imageRepository;
    }

    private Page<ProductResponse> getProducts(Pageable pageable, Supplier<Page<Object[]>> dataSupplier) {
        Page<Object[]> rawDataPage = dataSupplier.get();
        List<ProductResponse> productResponses = new ArrayList<>();

        for (Object[] row : rawDataPage.getContent()) {
            Long productId = (Long) row[0];
            String productName = (String) row[1];
            BigDecimal maxSalePrice = (BigDecimal) row[2];
            BigDecimal minDiscountedPrice = (BigDecimal) row[3];
            Integer discountValue = row[4] == null ? 0 : (Integer) row[4];
            List<ImageReponse> images = productId != null ?
                    this.imageRepository.getImageByProductIds(productId) : null;

            productResponses.add(
                    new ProductResponse(productId, productName, maxSalePrice, minDiscountedPrice, discountValue, images)
            );
        }
        return new PageImpl<>(productResponses, pageable, rawDataPage.getTotalElements());
    }

    public Page<ProductResponse> getFeaturedProducts(Pageable pageable, Integer category) {
        return getProducts(pageable, () -> productRepository.getFeaturedProductsData(category, pageable));
    }

    public Page<ProductResponse> getTopSellingProducts(Pageable pageable) {
        return getProducts(pageable, () -> productRepository.getTopSellingProductsData(pageable));
    }

    public Page<ProductResponse> getOfferProducts(Pageable pageable) {
        return getProducts(pageable, () -> productRepository.getOfferingProductsData(pageable));
    }
}
