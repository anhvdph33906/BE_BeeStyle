
package com.datn.beestyle.service.material;

import com.datn.beestyle.common.GenericServiceAbstract;
import com.datn.beestyle.common.IGenericMapper;
import com.datn.beestyle.common.IGenericRepository;
import com.datn.beestyle.dto.PageResponse;
import com.datn.beestyle.dto.material.CreateMaterialRequest;
import com.datn.beestyle.dto.material.MaterialResponse;
import com.datn.beestyle.dto.material.UpdateMaterialRequest;
import com.datn.beestyle.entity.product.attributes.Material;
import com.datn.beestyle.enums.Status;
import com.datn.beestyle.exception.InvalidDataException;
import com.datn.beestyle.repository.MaterialRepository;
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
public class MaterialService
        extends GenericServiceAbstract<Material, Integer, CreateMaterialRequest, UpdateMaterialRequest, MaterialResponse>
        implements IMaterialService {
    private final MaterialRepository materialRepository;

    public MaterialService(IGenericRepository<Material, Integer> entityRepository,
                           IGenericMapper<Material, CreateMaterialRequest, UpdateMaterialRequest, MaterialResponse> mapper,
                           EntityManager entityManager, MaterialRepository materialRepository) {
        super(entityRepository, mapper, entityManager);
        this.materialRepository = materialRepository;
    }

    public PageResponse<?> getAllByNameAndStatus(Pageable pageable, String name, String status) {
        int page = 0;
        if (pageable.getPageNumber() > 0) page = pageable.getPageNumber() - 1;

        Integer statusValue = null;
        if(status != null) {
            Status statusEnum = Status.fromString(status.toUpperCase());
            if (statusEnum != null) statusValue = statusEnum.getValue();
        }

        PageRequest pageRequest = PageRequest.of(page , pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt", "id"));

        Page<Material> materialPage = materialRepository.findByNameContainingAndStatus(pageRequest, name, statusValue);
        List<MaterialResponse> materialResponseList = mapper.toEntityDtoList(materialPage.getContent());
        return PageResponse.builder()
                .pageNo(pageRequest.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .totalElements(materialPage.getTotalElements())
                .totalPages(materialPage.getTotalPages())
                .items(materialResponseList)
                .build();
    }

    @Override
    public List<MaterialResponse> getAllByStatusIsActive() {
        return materialRepository.findAllByStatusIsActive();
    }

    @Override
    protected List<CreateMaterialRequest> beforeCreateEntities(List<CreateMaterialRequest> requests) {
        return requests;
    }

    @Override
    protected List<UpdateMaterialRequest> beforeUpdateEntities(List<UpdateMaterialRequest> requests) {
        List<UpdateMaterialRequest> validRequests = requests.stream().filter(dto -> dto.getId() != null).toList();
        if (validRequests.isEmpty()) return Collections.emptyList();

        List<Integer> ids = validRequests.stream().map(UpdateMaterialRequest::getId).toList();

        List<Integer> existingIds = materialRepository.findAllById(ids).stream().map(Material::getId).toList();
        if (existingIds.isEmpty()) return Collections.emptyList();

        return validRequests.stream().filter(dto -> existingIds.contains(dto.getId())).toList();
    }

    @Override
    protected void beforeCreate(CreateMaterialRequest request) {
        String materialName = request.getMaterialName().trim();
        if (materialRepository.existsByMaterialName(materialName))
            throw new InvalidDataException("Tên chất liệu đã tồn tại.");
        request.setMaterialName(materialName);
    }

    @Override
    protected void beforeUpdate(Integer id, UpdateMaterialRequest request) {

    }

    @Override
    protected void afterConvertCreateRequest(CreateMaterialRequest request, Material entity) {

    }

    @Override
    protected void afterConvertUpdateRequest(UpdateMaterialRequest request, Material entity) {

    }

    @Override
    protected String getEntityName() {
        return "Material";
    }


    @Override
    public List<MaterialResponse> getAllById(Set<Integer> integers) {
        return null;
    }
}
