package com.datn.beestyle.service.category;

import com.datn.beestyle.common.IGenericService;
import com.datn.beestyle.dto.PageResponse;
import com.datn.beestyle.dto.category.CategoryResponse;
import com.datn.beestyle.dto.category.CreateCategoryRequest;
import com.datn.beestyle.dto.category.UpdateCategoryRequest;
import com.datn.beestyle.dto.category.UserCategoryResponse;
import com.datn.beestyle.entity.Category;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICategoryService
        extends IGenericService<Category, Integer, CreateCategoryRequest, UpdateCategoryRequest, CategoryResponse> {
    List<UserCategoryResponse> getCategoryOptions();
    PageResponse<List<CategoryResponse>> getAllForAdmin(Pageable pageable, String name, String status);
}
