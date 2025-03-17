package com.datn.beestyle.common;

import com.datn.beestyle.dto.PageResponse;
import com.datn.beestyle.exception.ResourceNotFoundException;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public abstract class GenericServiceAbstract<T, ID, C, U, R> implements IGenericService<T, ID, C, U, R> {

    protected final IGenericRepository<T, ID> entityRepository;
    protected final IGenericMapper<T, C, U, R> mapper;
    protected final EntityManager entityManager;


    @Override
    public PageResponse<?> getAll(Pageable pageable) {
        int page = 0;
        if (pageable.getPageNumber() > 0) page = pageable.getPageNumber() - 1;

        PageRequest pageRequest = PageRequest.of(page, pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt", "id"));

        Page<T> entityPage = entityRepository.findAll(pageRequest);
        List<R> result = mapper.toEntityDtoList(entityPage.getContent());
        return PageResponse.builder()
                .pageNo(pageRequest.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .totalElements(entityPage.getTotalElements())
                .totalPages(entityPage.getTotalPages())
                .items(result)
                .build();
    }

    @Transactional
    @Override
    public R create(C request) {
        this.beforeCreate(request);
        T entity = mapper.toCreateEntity(request);
        this.afterConvertCreateRequest(request, entity);
        return mapper.toEntityDto(entityRepository.save(entity));
    }

    @Transactional
    @Override
    public R update(ID id, U request) {
        T entity = this.getById(id);
        this.beforeUpdate(id, request);
        mapper.toUpdateEntity(entity, request);
        this.afterConvertUpdateRequest(request, entity);
        return mapper.toEntityDto(entityRepository.save(entity));
    }

    @Transactional
    @Override
    public List<R> createEntities(List<C> requests) {
        if (requests.isEmpty()) return Collections.emptyList();
        List<C> entitiesToCreate = this.beforeCreateEntities(requests);
        List<T> entities = mapper.toCreateEntityList(entitiesToCreate);
        return mapper.toEntityDtoList(entityRepository.saveAll(entities));
    }

    @Transactional
    @Override
    public void updateEntities(List<U> requests) {
        if (requests.isEmpty()) return;
        List<T> entitiesToUpdate = mapper.toUpdateEntityList(this.beforeUpdateEntities(requests));
        if (entitiesToUpdate.isEmpty()) return;
        entityRepository.saveAll(entitiesToUpdate);
    }

    @Override
    public List<R> getAllById(Set<ID> ids) {
        return mapper.toEntityDtoList(entityRepository.findAllById(ids));
    }

    @Override
    public void delete(ID id) {
        entityRepository.deleteById(id);
    }

    @Override
    public R getDtoById(ID id) {
        return mapper.toEntityDto(this.getById(id));
    }

    @Override
    public T getById(ID id) {
        return entityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(this.getEntityName() + " not found."));
    }

    protected abstract List<C> beforeCreateEntities(List<C> requests);
    protected abstract List<U> beforeUpdateEntities(List<U> requests);
    protected abstract void beforeCreate(C request);
    protected abstract void beforeUpdate(ID id, U request);
    protected abstract void afterConvertCreateRequest(C request, T entity);
    protected abstract void afterConvertUpdateRequest(U request, T entity);
    protected abstract String getEntityName();

}
