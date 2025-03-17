package com.datn.beestyle.service.user;

import com.datn.beestyle.dto.cart.ShoppingCartRequest;
import com.datn.beestyle.dto.product.attributes.color.ColorResponse;
import com.datn.beestyle.dto.product.attributes.image.ImageReponse;
import com.datn.beestyle.dto.product.attributes.size.SizeResponse;
import com.datn.beestyle.dto.product.variant.ProductVariantResponse;
import com.datn.beestyle.repository.ColorRepository;
import com.datn.beestyle.repository.ImageRepository;
import com.datn.beestyle.repository.ProductVariantRepository;
import com.datn.beestyle.repository.SizeRepository;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserProductVariantService {

    private final ProductVariantRepository productVariantRepository;
    private final ImageRepository imageRepository;
    private final ColorRepository colorRepository;
    private final SizeRepository sizeRepository;

    public UserProductVariantService(
            ProductVariantRepository productVariantRepository,
            ImageRepository imageRepository, ColorRepository colorRepository,
            SizeRepository sizeRepository,
            HttpSession httpSession) {
        this.productVariantRepository = productVariantRepository;
        this.imageRepository = imageRepository;
        this.colorRepository = colorRepository;
        this.sizeRepository = sizeRepository;
    }

    public ProductVariantResponse getProductVariantUser(Long productId, String colorCode, Long sizeId) {
        ProductVariantResponse response = this.productVariantRepository
                .findProductVariantData(productId, colorCode, sizeId)
                .get(0);
        List<ImageReponse> images = productId != null ?
                this.imageRepository.getImageByProductIds(productId) : null;
        response.setImages(images);

        return response;
    }

    public List<ProductVariantResponse> getProductVariantByIds(
            List<ShoppingCartRequest> cartItemsRequest
    ) {
        List<Long> productVariantIds = cartItemsRequest.stream()
                .filter(cart -> cart != null && cart.getProductVariantId() != null)
                .map(ShoppingCartRequest::getProductVariantId)
                .distinct()
                .collect(Collectors.toList());

        if (productVariantIds.isEmpty()) {
            return new ArrayList<>();
        }

        List<ProductVariantResponse> responses = this.productVariantRepository
                .findProductVariantIds(productVariantIds);

        Map<Long, Integer> cartQuantityMap = cartItemsRequest.stream()
                .filter(cart -> cart != null && cart.getProductVariantId() != null && cart.getQuantity() != null)
                .collect(Collectors.toMap(ShoppingCartRequest::getProductVariantId, ShoppingCartRequest::getQuantity));

        for (ProductVariantResponse response : responses) {
            Integer quantity = cartQuantityMap.get(response.getId());
            if (quantity != null && response.getDiscountPrice() != null) {
                BigDecimal totalPrice = response.getDiscountPrice().multiply(BigDecimal.valueOf(quantity));
                response.setTotalPrice(totalPrice);
            } else {
                response.setTotalPrice(BigDecimal.ZERO);
            }
        }

        return responses;
    }

    public List<ColorResponse> getAllColorLists(Long productId) {
        return this.colorRepository.findAllByProductVariant(productId);
    }

    public List<SizeResponse> getAllSizeByPdAndColor(Long productId, String colorCode) {
        return this.sizeRepository.findAllByProductVariant(productId, colorCode);
    }
}
