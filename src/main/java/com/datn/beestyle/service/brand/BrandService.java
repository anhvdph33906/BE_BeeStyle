package com.datn.beestyle.service.brand;

import com.datn.beestyle.common.IGenericMapper;
import com.datn.beestyle.common.IGenericRepository;
import com.datn.beestyle.common.GenericServiceAbstract;
import com.datn.beestyle.dto.PageResponse;
import com.datn.beestyle.dto.brand.BrandResponse;
import com.datn.beestyle.dto.brand.CreateBrandRequest;
import com.datn.beestyle.dto.brand.UpdateBrandRequest;
import com.datn.beestyle.entity.product.attributes.Brand;
import com.datn.beestyle.enums.Status;
import com.datn.beestyle.exception.InvalidDataException;
import com.datn.beestyle.repository.BrandRepository;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class BrandService
        extends GenericServiceAbstract<Brand, Integer, CreateBrandRequest, UpdateBrandRequest, BrandResponse>
        implements IBrandService {

    private final BrandRepository brandRepository;

    public BrandService(IGenericRepository<Brand, Integer> entityRepository,
                        IGenericMapper<Brand, CreateBrandRequest, UpdateBrandRequest, BrandResponse> mapper,
                        EntityManager entityManager, BrandRepository brandRepository) {
        super(entityRepository, mapper, entityManager);
        this.brandRepository = brandRepository;
    }

    public PageResponse<?> getAllByNameAndStatus(Pageable pageable, String name, String status) {
        int page = 0;
        if (pageable.getPageNumber() > 0) page = pageable.getPageNumber() - 1;

        Integer statusValue = null;
        if(status != null) {
            Status statusEnum = Status.fromString(status.toUpperCase());
            if (statusEnum != null) statusValue = statusEnum.getValue();
        }

        PageRequest pageRequest = PageRequest.of(page, pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt", "id"));

        Page<Brand> materialPage = brandRepository.findByNameContainingAndStatus(pageRequest, name, statusValue);
        List<BrandResponse> materialResponseList = mapper.toEntityDtoList(materialPage.getContent());

        return PageResponse.builder()
                .pageNo(pageRequest.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .totalElements(materialPage.getTotalElements())
                .totalPages(materialPage.getTotalPages())
                .items(materialResponseList)
                .build();
    }

    @Override
    public List<BrandResponse> getAllByStatusIsActive() {
        return brandRepository.findAllByStatusIsActive();
    }

    @Override
    protected List<CreateBrandRequest> beforeCreateEntities(List<CreateBrandRequest> requests) {
        return requests;
    }

    @Override
    protected List<UpdateBrandRequest> beforeUpdateEntities(List<UpdateBrandRequest> requests) {
        List<UpdateBrandRequest> validRequests = requests.stream().filter(dto -> dto.getId() != null).toList();
        if (validRequests.isEmpty()) return Collections.emptyList();

        List<Integer> ids = validRequests.stream().map(UpdateBrandRequest::getId).toList();

        List<Integer> existingIds = brandRepository.findAllById(ids).stream().map(Brand::getId).toList();
        if (existingIds.isEmpty()) return Collections.emptyList();

        return validRequests.stream().filter(dto -> existingIds.contains(dto.getId())).toList();
    }


    @Override
    protected void beforeCreate(CreateBrandRequest request) {
        String brandName = request.getBrandName().trim();
        if (brandRepository.existsByBrandName(brandName))
            throw new InvalidDataException("Tên thương hiệu đã tồn tại.");
        request.setBrandName(brandName);
    }

    @Override
    protected void beforeUpdate(Integer id, UpdateBrandRequest request) {

    }

    @Override
    protected void afterConvertCreateRequest(CreateBrandRequest request, Brand entity) {

    }

    @Override
    protected void afterConvertUpdateRequest(UpdateBrandRequest request, Brand entity) {

    }

    @Override
    protected String getEntityName() {
        return "Brand";
    }

    @Override
    public List<BrandResponse> getAllById(Set<Integer> integers) {
        return null;
    }
}
