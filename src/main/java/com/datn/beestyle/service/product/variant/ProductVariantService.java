package com.datn.beestyle.service.product.variant;

import com.datn.beestyle.common.GenericServiceAbstract;
import com.datn.beestyle.common.IGenericMapper;
import com.datn.beestyle.common.IGenericRepository;
import com.datn.beestyle.dto.PageResponse;
import com.datn.beestyle.dto.product.variant.CreateProductVariantRequest;
import com.datn.beestyle.dto.product.variant.PatchUpdateQuantityProductVariant;
import com.datn.beestyle.dto.product.variant.ProductVariantResponse;
import com.datn.beestyle.dto.product.variant.UpdateProductVariantRequest;
import com.datn.beestyle.entity.product.ProductVariant;
import com.datn.beestyle.enums.Status;
import com.datn.beestyle.enums.StockAction;
import com.datn.beestyle.exception.InvalidDataException;
import com.datn.beestyle.repository.ProductVariantRepository;
import com.datn.beestyle.util.AppUtils;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductVariantService
        extends GenericServiceAbstract<ProductVariant, Long, CreateProductVariantRequest, UpdateProductVariantRequest, ProductVariantResponse>
        implements IProductVariantService {

    private final ProductVariantRepository productVariantRepository;

    public ProductVariantService(IGenericRepository<ProductVariant, Long> entityRepository,
                                 IGenericMapper<ProductVariant, CreateProductVariantRequest, UpdateProductVariantRequest, ProductVariantResponse> mapper,
                                 EntityManager entityManager, ProductVariantRepository productVariantRepository) {
        super(entityRepository, mapper, entityManager);
        this.productVariantRepository = productVariantRepository;
    }

    @Override
    public PageResponse<List<ProductVariantResponse>> getProductVariantsByFieldsByProductId(Pageable pageable, String productIdStr,
                                                                                            String keyword, String colorIds,
                                                                                            String sizeIds, String status) {
        Integer productId = null;
        if (productIdStr != null) {
            try {
                productId = Integer.parseInt(productIdStr);
            } catch (Exception e) {
                throw new IllegalArgumentException("ID sản phẩm phải là số.");
            }
        }

        int page = 0;
        if (pageable.getPageNumber() > 0) page = pageable.getPageNumber() - 1;
        PageRequest pageRequest = PageRequest.of(page, pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt", "id"));

        List<Integer> colorIdList = AppUtils.handleStringIdsToIntegerIdList(colorIds);
        List<Integer> sizeIdList = AppUtils.handleStringIdsToIntegerIdList(sizeIds);

        Integer statusValue = null;
        if (status != null) {
            Status statusEnum = Status.fromString(status);
            if (statusEnum != null) statusValue = statusEnum.getValue();
        }

        Page<ProductVariantResponse> productVariantResponsePages =
                productVariantRepository.findAllByFieldsByProductId(pageRequest, productId, keyword, colorIdList,
                        sizeIdList, statusValue);

        return PageResponse.<List<ProductVariantResponse>>builder()
                .pageNo(pageRequest.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .totalElements(productVariantResponsePages.getTotalElements())
                .totalPages(productVariantResponsePages.getTotalPages())
                .items(productVariantResponsePages.getContent())
                .build();
    }

    @Override
    public PageResponse<List<ProductVariantResponse>> filterProductVariantsByStatusIsActive(Pageable pageable, String productIdStr,
                                                                                            String colorIds, String sizeIds,
                                                                                            BigDecimal minPrice, BigDecimal maxPrice) {
        Integer productId = null;
        if (productIdStr != null) {
            try {
                productId = Integer.parseInt(productIdStr);
            } catch (Exception e) {
                throw new IllegalArgumentException("ID sản phẩm phải là số.");
            }
        }

        int page = 0, pageSize = 10;
        if (pageable.getPageNumber() > 0) page = pageable.getPageNumber() - 1;
        if (pageable.getPageSize() > 0) pageSize = pageable.getPageSize();
        PageRequest pageRequest = PageRequest.of(page, pageSize);

        List<Integer> colorIdList = AppUtils.handleStringIdsToIntegerIdList(colorIds);
        List<Integer> sizeIdList = AppUtils.handleStringIdsToIntegerIdList(sizeIds);

        Page<ProductVariantResponse> productVariantResponsePages =
                productVariantRepository.filterProductVariantByProductId(pageRequest, productId, colorIdList, sizeIdList,
                        minPrice, maxPrice, 1);

        return PageResponse.<List<ProductVariantResponse>>builder()
                .pageNo(pageRequest.getPageNumber() + 1)
                .pageSize(pageRequest.getPageSize())
                .totalElements(productVariantResponsePages.getTotalElements())
                .totalPages(productVariantResponsePages.getTotalPages())
                .items(productVariantResponsePages.getContent())
                .build();
    }

    @Override
    public int updateQuantityProductVariant(PatchUpdateQuantityProductVariant request, String action) {
        ProductVariant productVariant = this.getById(request.getId());

        int quantityInStock = productVariant.getQuantityInStock();

        if (action.equalsIgnoreCase(StockAction.PLUS_STOCK.name())) { // hồi sản phẩm trong kho
            request.setQuantity(quantityInStock + request.getQuantity());
        } else if (action.equalsIgnoreCase(StockAction.MINUS_STOCK.name())) { // trừ sản phẩm trong kho
            if (quantityInStock == 0) throw new RuntimeException("Sản phẩm đã hết hàng");
            if (request.getQuantity() > quantityInStock) {
                throw new RuntimeException("Số lượng mua vượt quá số lượng tồn kho. Vui lòng giảm số lượng hoặc chọn sản phẩm khác.");
            }
            request.setQuantity(quantityInStock - request.getQuantity());
        } else {
            throw new IllegalArgumentException("Hành động không hợp lệ ('plus' hoặc 'minus').");
        }

        return productVariantRepository.updateQuantityProductVariant(request.getId(), request.getQuantity());
    }

    @Override
    protected List<CreateProductVariantRequest> beforeCreateEntities(List<CreateProductVariantRequest> requests) {
        return null;
    }

    @Override
    protected List<UpdateProductVariantRequest> beforeUpdateEntities(List<UpdateProductVariantRequest> requests) {
        List<UpdateProductVariantRequest> validRequests = requests.stream()
                .filter(dto -> dto.getId() != null)
                .toList();
        if (validRequests.isEmpty()) return Collections.emptyList();

        List<Long> ids = validRequests.stream()
                .map(dto -> dto.getId().longValue())
                .toList();

        // Lấy các ID đã tồn tại từ repository
        List<Long> existingIds = productVariantRepository.findAllById(ids).stream()
                .map(ProductVariant::getId)
                .toList();

        if (existingIds.isEmpty()) return Collections.emptyList();

        return validRequests.stream()
                .filter(dto -> existingIds.contains(Long.valueOf(dto.getId())))
                .toList();
    }

    @Override
    @Transactional
    public void updateProductVariantCreate(Integer promotionId, List<Integer> ids) {
        productVariantRepository.updatePromotionForVariants(promotionId, ids);
    }
//    @Override
//    @Transactional
//    public void updateProductVariantUpdate(Integer promotionId, List<Integer> ids) {
//        productVariantRepository.updatePromotionToNullForNonSelectedIds(promotionId, ids);
//
//        try {
//            Thread.sleep(100);  // Dừng 100ms trước khi cập nhật
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
//
//        productVariantRepository.updatePromotionForVariants(promotionId, ids);
//    }

    public Map<String, List<Long>> getProductAndDetailIdsByPromotionId(Long promotionId) {
        List<Object[]> results = productVariantRepository.findProductAndDetailIdsByPromotionId(promotionId);

        List<Long> productIds = results.stream()
                .map(result -> (Long) result[0]) // `productId` là phần tử đầu trong `Object[]`
                .distinct()
                .collect(Collectors.toList());

        List<Long> productDetailIds = results.stream()
                .map(result -> (Long) result[1]) // `productDetailId` là phần tử thứ hai trong `Object[]`
                .collect(Collectors.toList());

        Map<String, List<Long>> response = new HashMap<>();
        response.put("productIds", productIds);
        response.put("productDetailIds", productDetailIds);

        return response;
    }

    @Override
    protected void beforeCreate(CreateProductVariantRequest request) {

    }

    @Override
    protected void beforeUpdate(Long aLong, UpdateProductVariantRequest request) {

    }

    @Override
    protected void afterConvertCreateRequest(CreateProductVariantRequest request, ProductVariant entity) {

    }

    @Override
    protected void afterConvertUpdateRequest(UpdateProductVariantRequest request, ProductVariant entity) {

    }

    @Override
    protected String getEntityName() {
        return "Product variant";
    }

    //    @Override
//    public Optional<Object[]> getAllProductsWithDetails(List<Long> productIds) {
//        return productVariantRepository.findAllProductsWithDetails(productIds);
//    }
//    @Override
//    public Optional<List<ProductVariantResponse>> getAllProductsWithDetails(List<Long> productIds) {
//        return productVariantRepository.findAllProductsWithDetails(productIds);
//    }
    @Override
    public Optional<Object[]> getAllProductsWithDetails(List<Long> productIds) {

        List<ProductVariantResponse> productVariantResponses = productVariantRepository.findAllProductsWithDetails(productIds);

        List<Object[]> result = productVariantResponses.stream()
                .map(response -> new Object[]{
                        response.getProductId(),
                        response.getProductName(),
                        response.getBrandName(),
                        response.getMaterialName(),
                        response.getId(),
                        response.getSku(),
                        response.getColorName(),
                        response.getSizeName(),
                        response.getOriginalPrice(),
                        response.getQuantityInStock(),
                        response.getPromotionName()
                })
                .collect(Collectors.toList());

        return Optional.ofNullable(result.toArray(new Object[0]));
    }


    // Phương thức xóa liên kết promotion với các bản ghi không được chọn
    @Transactional
    public void removePromotionFromNonSelectedVariants(Integer promotionId, Integer ids) {
        productVariantRepository.updatePromotionToNullForNonSelectedIds(promotionId, ids);
    }

    // Phương thức cập nhật promotion cho các bản ghi được chọn
    @Transactional
    public void applyPromotionToSelectedVariants(Integer promotionId, List<Integer> ids) {
        productVariantRepository.updatePromotionForVariants(promotionId, ids);
    }

}