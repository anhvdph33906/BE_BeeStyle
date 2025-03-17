package com.datn.beestyle.service.product.attributes.color;

import com.datn.beestyle.common.GenericServiceAbstract;
import com.datn.beestyle.common.IGenericMapper;
import com.datn.beestyle.common.IGenericRepository;
import com.datn.beestyle.dto.PageResponse;
import com.datn.beestyle.dto.product.attributes.color.ColorResponse;
import com.datn.beestyle.dto.product.attributes.color.CreateColorRequest;
import com.datn.beestyle.dto.product.attributes.color.UpdateColorRequest;
import com.datn.beestyle.entity.product.attributes.Color;
import com.datn.beestyle.enums.Status;
import com.datn.beestyle.exception.InvalidDataException;
import com.datn.beestyle.repository.ColorRepository;
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
public class ColorService
        extends GenericServiceAbstract<Color, Integer, CreateColorRequest, UpdateColorRequest, ColorResponse>
        implements IColorService {

    private final ColorRepository colorRepository;

    public ColorService(IGenericRepository<Color, Integer> entityRepository,
                        IGenericMapper<Color, CreateColorRequest, UpdateColorRequest, ColorResponse> mapper,
                        EntityManager entityManager, ColorRepository colorRepository) {
        super(entityRepository, mapper, entityManager);
        this.colorRepository = colorRepository;
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

        Page<Color> materialPage = colorRepository.findByNameContainingAndStatus(pageRequest, name, statusValue);
        List<ColorResponse> materialResponseList = mapper.toEntityDtoList(materialPage.getContent());

        return PageResponse.builder()
                .pageNo(pageRequest.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .totalElements(materialPage.getTotalElements())
                .totalPages(materialPage.getTotalPages())
                .items(materialResponseList)
                .build();
    }

    @Override
    public List<ColorResponse> getAllByStatusIsActive() {
        return colorRepository.findAllByStatusIsActive();
    }

    @Override
    protected List<CreateColorRequest> beforeCreateEntities(List<CreateColorRequest> requests) {
        return requests;
    }

    @Override
    protected List<UpdateColorRequest> beforeUpdateEntities(List<UpdateColorRequest> requests) {
        List<UpdateColorRequest> validRequests = requests.stream().filter(dto -> dto.getId() != null).toList();
        if (validRequests.isEmpty()) return Collections.emptyList();

        List<Integer> ids = validRequests.stream().map(UpdateColorRequest::getId).toList();

        List<Integer> existingIds = colorRepository.findAllById(ids).stream().map(Color::getId).toList();
        if (existingIds.isEmpty()) return Collections.emptyList();

        return validRequests.stream().filter(dto -> existingIds.contains(dto.getId())).toList();
    }

    @Override
    protected void beforeCreate(CreateColorRequest request) {
        String colorName = request.getColorName().trim();
        if (colorRepository.existsByColorName(colorName))
            throw new InvalidDataException("Tên màu sắc đã tồn tại.");
        request.setColorName(colorName);
    }

    @Override
    protected void beforeUpdate(Integer id, UpdateColorRequest request) {

    }

    @Override
    protected void afterConvertCreateRequest(CreateColorRequest request, Color entity) {

    }

    @Override
    protected void afterConvertUpdateRequest(UpdateColorRequest request, Color entity) {

    }

    @Override
    protected String getEntityName() {
        return "Color";
    }

    @Override
    public List<ColorResponse> getAllById(Set<Integer> integers) {
        return null;
    }
}
