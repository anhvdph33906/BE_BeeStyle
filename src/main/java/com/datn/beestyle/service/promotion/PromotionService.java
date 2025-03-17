package com.datn.beestyle.service.promotion;

import com.datn.beestyle.common.GenericServiceAbstract;
import com.datn.beestyle.common.IGenericMapper;
import com.datn.beestyle.common.IGenericRepository;
import com.datn.beestyle.dto.PageResponse;
import com.datn.beestyle.dto.promotion.CreatePromotionRequest;
import com.datn.beestyle.dto.promotion.PromotionResponse;
import com.datn.beestyle.dto.promotion.UpdatePromotionRequest;
import com.datn.beestyle.entity.Promotion;
import com.datn.beestyle.entity.Voucher;
import com.datn.beestyle.enums.DiscountStatus;
import com.datn.beestyle.enums.DiscountType;
import com.datn.beestyle.repository.ProductVariantRepository;
import com.datn.beestyle.repository.PromotionRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

@Service
public class PromotionService
        extends GenericServiceAbstract<Promotion, Integer, CreatePromotionRequest, UpdatePromotionRequest, PromotionResponse>
        implements IPromotionService {
    private final PromotionRepository promotionRepository;
    private final ProductVariantRepository productVariantRepository;

    public PromotionService(IGenericRepository<Promotion, Integer> entityRepository,
                            IGenericMapper<Promotion, CreatePromotionRequest, UpdatePromotionRequest, PromotionResponse> mapper,
                            EntityManager entityManager,
                            PromotionRepository promotionRepository, ProductVariantRepository productVariantRepository) {
        super(entityRepository, mapper, entityManager);
        this.promotionRepository = promotionRepository;
        this.productVariantRepository = productVariantRepository;
    }

    @Override
    public PageResponse<?> getAllByNameAndStatus(Pageable pageable, String name, String status, String discountType, Timestamp startDate, Timestamp endDate) {
        int page = 0;
        if (pageable.getPageNumber() > 0) page = pageable.getPageNumber() - 1;

        Integer statusValue = null;
        if (status != null) {
            DiscountStatus statusEnum = DiscountStatus.fromString(status.toUpperCase());
            if (statusEnum != null) statusValue = statusEnum.getValue();
        }
        Integer discountTypeValue = null;
        if (discountType != null) {
            DiscountType discountTypeEnum = DiscountType.fromString(discountType.toUpperCase());
            if (discountTypeEnum != null) discountTypeValue = discountTypeEnum.getValue();
        }
        PageRequest pageRequest = PageRequest.of(page, pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt", "id"));

        Page<PromotionResponse> promotionPage = promotionRepository.findByNameContainingAndStatus(pageRequest, name, statusValue, discountTypeValue, startDate, endDate);
//        List<PromotionResponse> promotionResponseList = mapper.toEntityDtoList(promotionPage.getContent());

        return PageResponse.builder()
                .pageNo(pageRequest.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .totalElements(promotionPage.getTotalElements())
                .totalPages(promotionPage.getTotalPages())
                .items(promotionPage.getContent())
                .build();
    }
//    @Override
//    public List<PromotionResponse> createPromotion(List<CreatePromotionRequest> requestList) {
//        List<Promotion> promotionList = mapper.toCreateEntityList(requestList);
//        return mapper.toEntityDtoList(promotionRepository.saveAll(promotionList));
//    }

    @Override
    protected List<CreatePromotionRequest> beforeCreateEntities(List<CreatePromotionRequest> requests) {
        return requests;
    }

    @Override
    protected List<UpdatePromotionRequest> beforeUpdateEntities(List<UpdatePromotionRequest> requests) {
        List<UpdatePromotionRequest> validRequests = requests.stream()
                .filter(dto -> dto.getId() != null)
                .toList();
        if (validRequests.isEmpty()) return Collections.emptyList();

        List<Integer> ids = validRequests.stream()
                .map(UpdatePromotionRequest::getId)
                .toList();

        List<Integer> existingIds = promotionRepository.findAllById(ids)
                .stream()
                .map(Promotion::getId)
                .toList();
        if (existingIds.isEmpty()) return Collections.emptyList();

        return validRequests.stream()
                .filter(dto -> existingIds.contains(dto.getId()))
                .toList();
    }

    @Override
    protected void beforeCreate(CreatePromotionRequest request) {
        // Implement any logic before creating a promotion
    }

    @Override
    protected void beforeUpdate(Integer id, UpdatePromotionRequest request) {
        // Implement any logic before updating a promotion
    }

    @Override
    protected void afterConvertCreateRequest(CreatePromotionRequest request, Promotion entity) {
        // Implement any logic after converting create request to entity
    }

    @Override
    protected void afterConvertUpdateRequest(UpdatePromotionRequest request, Promotion entity) {
        // Implement any logic after converting update request to entity
    }

    @Override
    protected String getEntityName() {
        return "Promotion";
    }

    @Transactional
    public void deletePromotion(Integer id) {
        // Cập nhật các bản ghi trong product_variant trước khi xóa promotion
        productVariantRepository.updateProductVariantToNullByPromotionId(id);
        // Sau đó, xóa promotion
        promotionRepository.deleteById(id);
    }


}
