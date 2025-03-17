package com.datn.beestyle.service.category;

import com.datn.beestyle.common.GenericServiceAbstract;
import com.datn.beestyle.common.IGenericMapper;
import com.datn.beestyle.common.IGenericRepository;
import com.datn.beestyle.dto.PageResponse;
import com.datn.beestyle.dto.category.CategoryResponse;
import com.datn.beestyle.dto.category.CreateCategoryRequest;
import com.datn.beestyle.dto.category.UpdateCategoryRequest;
import com.datn.beestyle.dto.category.UserCategoryResponse;
import com.datn.beestyle.entity.Category;
import com.datn.beestyle.enums.Status;
import com.datn.beestyle.exception.InvalidDataException;
import com.datn.beestyle.mapper.CategoryMapper;
import com.datn.beestyle.repository.CategoryRepository;
import com.datn.beestyle.util.AppUtils;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.datn.beestyle.util.AppUtils.MAX_CATEGORY_LEVEL;

@Slf4j
@Service
public class CategoryService
        extends GenericServiceAbstract<Category, Integer, CreateCategoryRequest, UpdateCategoryRequest, CategoryResponse>
        implements ICategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(IGenericRepository<Category, Integer> entityRepository,
                           IGenericMapper<Category, CreateCategoryRequest, UpdateCategoryRequest, CategoryResponse> mapper,
                           EntityManager entityManager, CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        super(entityRepository, mapper, entityManager);
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<UserCategoryResponse> getCategoryOptions() {
        List<Object[]> results = categoryRepository.findAllForUser();
        Map<Integer, UserCategoryResponse> userCategoryResponseMap = new HashMap<>();
        List<UserCategoryResponse> rootCategories = new ArrayList<>();

        for (Object[] row : results) {
            Integer id = (Integer) row[0];
            String categoryName = (String) row[1];
            String slug = (String) row[2];
            Integer parentId = (Integer) row[3];

            UserCategoryResponse userCategoryResponse = new UserCategoryResponse(id, categoryName, slug);
            userCategoryResponseMap.put(id, userCategoryResponse);

            if (parentId != null) {
                UserCategoryResponse parentCategory = userCategoryResponseMap.get(parentId);
                if (parentCategory != null) {
                    parentCategory.getCategoryChildren().add(userCategoryResponse);
                }
            } else {
                rootCategories.add(userCategoryResponse);
            }
        }
        return rootCategories;
    }

    @Override
    public PageResponse<List<CategoryResponse>> getAllForAdmin(Pageable pageable, String name, String status) {
        Map<Integer, String> categoryNames;
        List<CategoryResponse> categoryResponses;

        int page = 0;
        if (pageable.getPageNumber() > 0) page = pageable.getPageNumber() - 1;

        Integer statusValue = null;
        if (status != null) {
            Status statusEnum = Status.fromString(status);
            if (statusEnum != null) statusValue = statusEnum.getValue();
        }

        PageRequest pageRequest = PageRequest.of(page, pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt", "id"));

        Page<Category> categoryPages =
                categoryRepository.findAllByKeywordAndStatus(pageRequest, name, statusValue);

        List<Integer> ids = categoryPages.get().map(category ->
                category.getParentCategory() != null ? category.getParentCategory().getId() : null).distinct().toList();

        if (ids.isEmpty()) {
            categoryNames = null;
        } else {
            categoryNames = categoryRepository.findCategoryNameById(ids).stream()
                    .collect(Collectors.toMap(object -> (Integer) object[0], object -> (String) object[1]));
        }

        if (categoryNames != null) {
            categoryResponses = categoryPages.get().map(category -> {
                CategoryResponse categoryResponse = categoryMapper.toEntityDto(category);
                if (category.getParentCategory() != null) {
                    categoryResponse.setParentCategoryName(categoryNames.get(category.getParentCategory().getId()));
                } else {
                    categoryResponse.setParentCategoryName(null);
                }
                return categoryResponse;
            }).toList();
        } else {
            categoryResponses = categoryPages.get().map(categoryMapper::toEntityDto).toList();
        }

        return PageResponse.<List<CategoryResponse>>builder()
                .pageNo(pageRequest.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .totalElements(categoryPages.getTotalElements())
                .totalPages(categoryPages.getTotalPages())
                .items(categoryResponses)
                .build();
    }

    @Override
    protected List<CreateCategoryRequest> beforeCreateEntities(List<CreateCategoryRequest> requests) {
        return null;
    }

    @Override
    protected List<UpdateCategoryRequest> beforeUpdateEntities(List<UpdateCategoryRequest> requests) {
        return null;
    }

    @Override
    protected void beforeCreate(CreateCategoryRequest request) {
        // Kiểm tra tên danh mục đã tồn tại chưa
        String categoryName = request.getCategoryName().trim();
        if (categoryRepository.existsByCategoryName(categoryName))
            throw new InvalidDataException("Tên danh mục đã tồn tại.");
        request.setCategoryName(categoryName);

        // Xử lý slug: nếu không nhập tự sinh từ tên danh mục
        String slug = request.getSlug();
        if (StringUtils.hasText(slug)) {
            slug = AppUtils.toSlug(slug.trim());
            request.setSlug(slug);
        } else {
            slug = AppUtils.toSlug(categoryName);
            request.setSlug(slug);
        }
        if (categoryRepository.existsBySlug(slug)) throw new InvalidDataException("Slug danh mục đã tồn tại.");

        // xử lý level
        if (request.getParentCategoryId() == null) request.setLevel(1);

        long count = categoryRepository.countByLevelAndParentCategoryId(request.getLevel(), request.getParentCategoryId());
        if (count == 0) {
            request.setPriority(1);
        } else {
            request.setPriority((int) (count + 1));
        }
    }

    @Override
    protected void beforeUpdate(Integer id, UpdateCategoryRequest request) {
        request.setCategoryName(request.getCategoryName().trim());

        String slug = request.getSlug();
        if (StringUtils.hasText(slug)) {
            slug = AppUtils.toSlug(slug.trim());
            request.setSlug(slug);
        } else {
            slug = AppUtils.toSlug(request.getCategoryName().trim());
            request.setSlug(slug);
        }
    }

    @Override
    protected void afterConvertCreateRequest(CreateCategoryRequest request, Category entity) {
        if (request.getParentCategoryId() != null) {
            Optional<Category> parentCategory = categoryRepository.findById(request.getParentCategoryId());
            if (parentCategory.isEmpty()) throw new InvalidDataException("Parent category not found");

            // kiểm tra cấp danh mục, tránh category cấp 4
            int parentLevel = parentCategory.get().getLevel();
            if (parentLevel >= 3) throw new InvalidDataException("Cannot add a child category to a level 3 category");

            entity.setParentCategory(parentCategory.get());
            entity.setLevel(parentLevel + 1);
        }
    }

    @Override
    protected void afterConvertUpdateRequest(UpdateCategoryRequest request, Category updateCategory) {
        this.checkDuplicateCategory(request, updateCategory);
        // check change parent category
        if (request.getParentCategoryId() != null) {
            this.checkUpdateParentCategory(request, updateCategory);
        } else {
            updateCategory.setParentCategory(null);
            updateCategory.setLevel(1);
        }
    }

    @Override
    protected String getEntityName() {
        return "Category";
    }

    private boolean isParentChildLoop(Integer updateCategoryId, Integer parentCategoryId) {
        Long count = categoryRepository.isParentChildLoop(updateCategoryId, parentCategoryId);
        return count != null && count > 0;
    }

    private void checkDuplicateCategory(UpdateCategoryRequest request, Category updateCategory) {
        // Kiểm tra tên danh mục trùng lặp (không tính chính danh mục đang cập nhật)
        Optional<Category> categoryByName =
                categoryRepository.findByCategoryNameAndIdNot(request.getCategoryName(), updateCategory.getId());
        if (categoryByName.isPresent()) throw new InvalidDataException("Tên danh mục đã tồn tại.");

        // Kiểm tra slug trùng lặp (không tính chính danh mục đang cập nhật)
        String slug = request.getSlug();
        Optional<Category> categoryBySlug = categoryRepository.findBySlugAndIdNot(slug, updateCategory.getId());
        if (categoryBySlug.isPresent()) throw new InvalidDataException("Slug danh mục đã tồn tại.");
    }

    private void checkUpdateParentCategory(UpdateCategoryRequest request, Category updateCategory) {
        Integer currentParentCategoryId = updateCategory.getParentCategory() != null
                ? updateCategory.getParentCategory().getId()
                : null;

        // Nếu danh mục cha không thay đổi, bỏ qua các bước kiểm tra tiếp theo
        if (request.getParentCategoryId().equals(currentParentCategoryId)) return;

        // xử lý chặn thay đổi danh mục cha khi danh mục có danh mục con
        Integer updateCategoryId = updateCategory.getId();
        if (categoryRepository.existsChildCategoryByParentId(updateCategoryId)) {
            throw new InvalidDataException("Không thể thay đổi danh mục cha của danh mục đã có danh mục con. " +
                                           "Vui lòng di chuyển danh mục con sang danh mục khác trước.");
        }

        Optional<Category> parentCategory = categoryRepository.findById(request.getParentCategoryId());
        if (parentCategory.isEmpty()) throw new InvalidDataException("Parent category not found");
        Category category = parentCategory.get();

        // Không cho phép thay đổi thành danh mục cha có cấp độ bằng hoặc nhỏ hơn 3
        int parentLevel = category.getLevel();
        if (parentLevel >= MAX_CATEGORY_LEVEL) {
            throw new InvalidDataException("Không thể đặt danh mục cấp 3 làm danh mục cha.");
        }

        int parentId = category.getId();
        // xử lý danh mục không thể làm con chính nó
        if (updateCategory.getId().equals(parentId)) {
            throw new InvalidDataException("Không thể chọn danh mục làm cha cho chính nó.");
        } else {
            // xử lý tránh vòng lặp vô hạn
            if (this.isParentChildLoop(updateCategory.getId(), request.getParentCategoryId()))
                throw new InvalidDataException("Không thể chọn danh mục con của danh mục hiện tại làm cha cho danh mục.");
        }

        updateCategory.setParentCategory(category);
        updateCategory.setLevel(parentLevel + 1);
    }
}