package com.datn.beestyle.controller.user;

import com.datn.beestyle.dto.ApiResponse;
import com.datn.beestyle.service.user.UserProductHomeService;
import com.datn.beestyle.service.user.UserProductVariantService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@Tag(name = "User Product Controller")
public class UserProductController {

    private final UserProductHomeService productService;
    private final UserProductVariantService productVariantService;

    @GetMapping
    public ApiResponse<?> featuredProducts(
            @PageableDefault(size = 8) Pageable pageable,
            @RequestParam(name = "q", required = false) Integer category
    ) {
        return new ApiResponse<>(HttpStatus.OK.value(), "Products Area",
                productService.getFeaturedProducts(pageable, category)
        );
    }

    @GetMapping("/seller")
    public ApiResponse<?> sellingProducts(@PageableDefault() Pageable pageable) {
        return new ApiResponse<>(HttpStatus.OK.value(), "Products Seller",
                productService.getTopSellingProducts(pageable)
        );
    }

    @GetMapping("/offer")
    public ApiResponse<?> offeringProducts(@PageableDefault(size = 9) Pageable pageable) {
        return new ApiResponse<>(HttpStatus.OK.value(), "Products Offer",
                productService.getOfferProducts(pageable)
        );
    }

    @GetMapping("/{productId}/variant/color")
    public ApiResponse<?> getColorSingleProduct(@PathVariable Long productId) {
        return new ApiResponse<>(HttpStatus.OK.value(), "Products Colors Product Variant",
                productVariantService.getAllColorLists(productId)
        );
    }

    @GetMapping("/{productId}/variant/size")
    public ApiResponse<?> getSizeSingleProduct(
            @PathVariable Long productId,
            @RequestParam(name = "c") String colorCode
    ) {
        return new ApiResponse<>(HttpStatus.OK.value(), "Products Sizes Product Variant",
                productVariantService.getAllSizeByPdAndColor(productId, colorCode)
        );
    }

    @GetMapping("/{productId}/variant")
    public ApiResponse<?> getProductVariant(
            @PathVariable Long productId,
            @RequestParam(name = "c", required = false) String colorCode,
            @RequestParam(name = "s", required = false) Long sizeId
    ) {
        return new ApiResponse<>(HttpStatus.OK.value(), "Products Variant",
                productVariantService.getProductVariantUser(productId, colorCode, sizeId)
        );
    }
}
